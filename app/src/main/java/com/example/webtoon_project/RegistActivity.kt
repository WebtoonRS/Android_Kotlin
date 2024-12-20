package com.example.webtoon_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.webtoon_project.databinding.ActivitySignBinding
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegistActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    private lateinit var myAPI: INodeJS
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize API
        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(INodeJS::class.java)

        // Set button click listener
        binding.registerButtonM.setOnClickListener {
            registerUser(
                binding.REdtEmail.text.toString(),
                binding.REdtPassword.text.toString(),
                binding.REdtName.text.toString()
            )
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun registerUser(email: String, password: String, name: String) {
        compositeDisposable.add(myAPI.registerUser(email, password, name)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                // 회원가입 성공 후 LoginActivity로 이동
                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra("response", response)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish() // 현재 Activity 종료하여 루프 방지
            }, { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        )
    }
}
