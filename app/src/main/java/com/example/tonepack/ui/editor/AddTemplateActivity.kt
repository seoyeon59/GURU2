package com.example.tonepack.ui.editor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.tonepack.R

class AddTemplateActivity : AppCompatActivity() {

    private val viewModel: EditorViewModel by viewModels()

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var spinnerSituation: Spinner
    private lateinit var spinnerTarget: Spinner
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_template)

        initViews()
        setupSpinners()
        observeViewModel()

        // 저장 버튼 클릭 시 데이터 전달 및 저장 요청
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            val situation = spinnerSituation.selectedItem.toString()
            val target = spinnerTarget.selectedItem.toString()

            // 로그를 찍어서 한글이 제대로 찍히는지 확인
            android.util.Log.d("AddTemplate", "입력 데이터: $title, $content")

            viewModel.saveTemplate(title, content, situation, target)
        }
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTemplateTitle)
        etContent = findViewById(R.id.etTemplateContent)
        spinnerSituation = findViewById(R.id.spinnerSituation)
        spinnerTarget = findViewById(R.id.spinnerTarget)
        btnSave = findViewById(R.id.btnSaveTemplate)
    }

    // Spinner 데이터 세팅: 글씨 색상 화이트 적용을 위해 커스텀 레이아웃 연결
    private fun setupSpinners() {
        // 1. 상황 스피너 설정
        val situationArray = resources.getStringArray(R.array.situation_categories)
        val situationAdapter = ArrayAdapter(this, R.layout.spinner_item, situationArray)

        // 드롭다운 목록도 커스텀 레이아웃 사용
        situationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerSituation.adapter = situationAdapter

        // 2. 상대 스피너 설정
        val targetArray = resources.getStringArray(R.array.target_categories)
        val targetAdapter = ArrayAdapter(this, R.layout.spinner_item, targetArray)

        targetAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerTarget.adapter = targetAdapter
    }

    // 저장 결과에 따른 UI 처리 (성공 시 화면 종료)
    private fun observeViewModel() {
        viewModel.saveSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "템플릿이 저장되었습니다! 💾", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}