import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.webtoon_project.databinding.ActivityLoginBinding //바인딩 이름 확인 (xml 생성시 자동 부여)
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import com.example.webtoon_project.RegistActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myAPI: INodeJS
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize API
        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(INodeJS::class.java)

        // Set button click listeners
        binding.loginButton.setOnClickListener {
            loginUser(binding.editEmail.text.toString(), binding.editPw.text.toString())
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        }
    }
test
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
    private fun loginUser(email: String, password: String) {
        compositeDisposable.add(myAPI.loginUser(email, password)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response?.contains("encrypted_password") == true) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, response ?: "No response", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        )
    }

}