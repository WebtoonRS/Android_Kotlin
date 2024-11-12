import sys
import json
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

def load_data():
    """CSV 파일에서 데이터와 임베딩을 함께 로드합니다."""
    df = pd.read_csv('merged_data.csv')
    
    # 임베딩 컬럼 분리
    embedding_columns = [col for col in df.columns if col.startswith('embedding_')]
    embeddings = df[embedding_columns].values
    
    return df, embeddings

def get_recommendations(user_data):
    try:
        webtoon_id = user_data.get('webtoon_id')
        if not webtoon_id:
            raise ValueError("webtoon_id is required")

        # 데이터 로드
        df, embeddings = load_data()
        
        # 선택된 웹툰의 인덱스 찾기
        selected_idx = df[df['id'] == int(webtoon_id)].index[0]
        
        # 선택된 웹툰의 임베딩으로 유사도 계산
        selected_embedding = embeddings[selected_idx].reshape(1, -1)
        cosine_similarities = cosine_similarity(selected_embedding, embeddings).flatten()
        
        # 자기 자신 제외하고 상위 9개 추천
        similar_indices = np.argsort(cosine_similarities)[::-1]
        similar_indices = similar_indices[similar_indices != selected_idx][:9]
        
        recommendations = {
            "recommendations": [
                {
                    "id": int(selected_idx + 1),  # 인덱스를 ID로 사용
                    "title": df.iloc[idx]['title'],
                    "thumbnail_link": df.iloc[idx]['Thumb'],
                    "synopsis": df.iloc[idx]['synopsis'],
                    "similarity_score": float(cosine_similarities[idx])
                } for idx in similar_indices
            ]
        }
        
        print(json.dumps(recommendations))
        
    except Exception as e:
        error_response = {
            "error": str(e)
        }
        print(json.dumps(error_response))

if __name__ == "__main__":
    try:
        user_data = json.loads(sys.argv[1])
        get_recommendations(user_data)
    except Exception as e:
        print(json.dumps({"error": f"Failed to parse input: {str(e)}"})) 