package com.example.webtoon_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.webtoon_project.databinding.ActivityStartBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(3000)
            startActivity(Intent(this@StartActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish() // StartActivity 종료하여 다시 돌아오지 않도록 설정 (일단 일회성으로만)
        }
    }
}
