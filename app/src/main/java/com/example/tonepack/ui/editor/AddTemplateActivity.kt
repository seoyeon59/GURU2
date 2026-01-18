package com.example.tonepack.ui.editor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tonepack.databinding.ActivityAddTemplateBinding

class AddTemplateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTemplateBinding

    // 민경님이 만든 EditorViewModel 연결!
    private val viewModel: EditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 저장 버튼 클릭 이벤트
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            // 제목과 내용이 비어있지 않은지 확인
            if (title.isNotBlank() && content.isNotBlank()) {
                viewModel.saveTemplate(title, content)
                Toast.makeText(this, "템플릿이 저장되었습니다!", Toast.LENGTH_SHORT).show()
                finish() // 저장 후 화면 닫기
            } else {
                Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}