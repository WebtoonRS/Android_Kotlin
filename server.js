const express = require('express');
const app = express();
const port = 3000;
const fs = require('fs');
const csv = require('csv-parser');
const { spawn } = require('child_process');

app.use(express.json());

let webtoons = [];

// merged_data.csv 파일 읽기
fs.createReadStream('merged_data.csv')
  .pipe(csv())
  .on('data', (data) => {
    // 임베딩 컬럼 분리
    const embedding = Object.keys(data)
      .filter(key => key.startsWith('embedding_'))
      .reduce((obj, key) => {
        obj[key] = data[key];
        return obj;
      }, {});

    const webtoon = {
        id: webtoons.length + 1,
        title: data.title,
        author: data.author,
        star_score: parseFloat(data.star_score),
        genre: data.genre,
        keywords: data.keywords.split(',').map(k => k.trim()),
        synopsis: data.synopsis,
        thumbnail_link: data.Thumb,
        embedding: embedding
    };
    webtoons.push(webtoon);
  });

// 기본 웹툰 API
app.get('/api/webtoons', (req, res) => {
    res.json(webtoons);
});

// 웹툰 검색 및 추천
app.get('/api/webtoons/search', async (req, res) => {
    const query = req.query.query.toLowerCase();
    
    // 기본 검색 결과
    const searchResults = webtoons.filter(webtoon => 
        webtoon.title.toLowerCase().includes(query) ||
        webtoon.author.toLowerCase().includes(query) ||
        webtoon.synopsis.toLowerCase().includes(query) ||
        webtoon.keywords.some(keyword => keyword.toLowerCase().includes(query))
    );

    if (searchResults.length > 0) {
        // 첫 번째 검색 결과를 기반으로 추천
        const firstResult = searchResults[0];
        
        // Python 스크립트 실행하여 추천 받기
        const pythonProcess = spawn('python', [
            'recommendation.py',
            JSON.stringify({ webtoon_id: firstResult.id })
        ]);

        let pythonData = "";

        pythonProcess.stdout.on('data', (data) => {
            pythonData += data.toString();
        });

        pythonProcess.stderr.on('data', (data) => {
            console.error(`Python Error: ${data}`);
        });

        await new Promise((resolve, reject) => {
            pythonProcess.on('close', (code) => {
                if (code !== 0) {
                    reject('Recommendation failed');
                }
                try {
                    const recommendations = JSON.parse(pythonData);
                    // 검색 결과와 추천 결과 합치기
                    const response = {
                        searchResult: firstResult,
                        recommendations: recommendations.recommendations
                    };
                    res.json(response);
                    resolve();
                } catch (error) {
                    reject('Failed to parse recommendations');
                }
            });
        });
    } else {
        res.json({ searchResult: null, recommendations: [] });
    }
});

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
}); 