package com.example.webtoonsearch

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                // 검색 로직 추가
                Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }
}