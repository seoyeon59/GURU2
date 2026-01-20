package com.example.tonepack.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tonepack.App
import com.example.tonepack.ui.main.MainActivity
import com.example.tonepack.databinding.ActivityLoginBinding // ViewBinding 사용 가정
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // 뷰바인딩을 쓰면 btnLogin을 바로 찾을 수 있어 편합니다.
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // App 클래스에서 싱글톤으로 만든 Repository와 SessionManager 가져오기
        val app = application as App
        val authRepository = app.authRepository
        val sessionManager = app.sessionManager

        // 이미 로그인 되어 있다면 바로 메인으로 이동
        if (sessionManager.isLoggedIn()) {
            moveToMain()
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPw.text.toString()

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 코루틴을 사용해 비동기로 로그인 체크
            lifecycleScope.launch {
                val result = authRepository.login(id, pw)

                if (result.isSuccess) {
                    val user = result.getOrNull()
                    if (user != null) {
                        // 세션 저장 후 이동
                        sessionManager.saveLoginSession(user.id)
                        moveToMain()
                    }
                } else {
                    // 실패 시 에러 메시지 출력
                    Toast.makeText(this@LoginActivity, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // 로그인 화면은 종료
    }
}