// package com.example.webtoon_project

// import android.content.Intent
// import android.os.Bundle
// import androidx.appcompat.app.AppCompatActivity
// import com.example.webtoon_project.databinding.ActivityCheckPwBinding

// class CheckPwActivity : AppCompatActivity() {
//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         val binding = ActivityCheckPwBinding.inflate(layoutInflater)
//         setContentView(binding.root)

//         binding.findPw.setOnClickListener {
//             val email = binding.editEmailFind.text.toString().trim()

//             if (email.isNotEmpty()) {
//                 val resultText = "${email}pw\n비밀번호를 1234로 초기화합니다."
//                 binding.findResult.text = resultText
//             }
//         }

//         binding.returnLogin.setOnClickListener {
//             val email = binding.editEmailFind.text.toString().trim()
//             val password = "1234"

//             if (email.isNotEmpty()) {
//                 val resultIntent = Intent()
//                 resultIntent.putExtra("EMAIL", email)
//                 resultIntent.putExtra("PASSWORD", password)
//                 setResult(RESULT_OK, resultIntent)
//                 finish()
//             }
//         }
//     }
// } 