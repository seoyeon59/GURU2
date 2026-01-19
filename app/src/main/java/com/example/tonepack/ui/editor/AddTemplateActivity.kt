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

/**
 * 템플릿 작성 화면 Activity
 *
 * 기능:
 *  1. 제목, 내용 입력받기 (EditText)
 *  2. 상황, 상대 선택받기 (Spinner)
 *  3. 저장 버튼 클릭 시 ViewModel을 통해 DB에 저장
 *  4. 저장 성공 시 화면 닫기, 실패 시 에러 메시지 표시
 *
 */
class AddTemplateActivity : AppCompatActivity() {

    // ViewModel 연결 (자동으로 EditorViewModel 생성)
    private val viewModel: EditorViewModel by viewModels()

    // UI 요소
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var spinnerSituation: Spinner
    private lateinit var spinnerTarget: Spinner
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_template)

        // 1. XML의 뷰들을 코드와 연결
        initViews()

        // 2. Spinner에 선택 항목 설정 (상황, 상대)
        setupSpinners()

        // 3. ViewModel의 결과 메시지 관찰
        observeViewModel()

        // 4. 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            val situation = spinnerSituation.selectedItem.toString()
            val target = spinnerTarget.selectedItem.toString()

            // ViewModel에게 저장 요청 (검증은 ViewModel에서 수행)
            viewModel.saveTemplate(title, content, situation, target)
        }
    }

    /**
     * XML의 View들을 findViewById로 연결
     */
    private fun initViews() {
        etTitle = findViewById(R.id.etTemplateTitle)
        etContent = findViewById(R.id.etTemplateContent)
        spinnerSituation = findViewById(R.id.spinnerSituation)
        spinnerTarget = findViewById(R.id.spinnerTarget)
        btnSave = findViewById(R.id.btnSaveTemplate)
    }

    /**
     * Spinner 설정: 상황과 상대 선택 목록 설정
     *
     * 상황: 출근, 업무, 퇴사, 실수보고, 감사·인사, 문의
     * 상대: 상사, 동료, 동기, 업체, 후배, 박사님·교수님, 학생회장, 학과
     */
    private fun setupSpinners() {
        // 상황 선택 스피너
        // [협업] 서연님의 SeedData.kt와 카테고리 일치시킴
        val situations = arrayOf(
            "선택하세요",
            "출근",
            "업무",
            "퇴사",
            "실수보고",
            "감사, 인사",
            "문의"
        )
        val situationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            situations
        )
        situationAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerSituation.adapter = situationAdapter

        // 상대 선택 스피너
        // 서연님의 SeedData.kt와 카테고리 일치시킴
        val targets = arrayOf(
            "선택하세요",
            "상사",
            "동료",
            "동기",
            "업체",
            "후배",
            "박사님, 교수님",
            "학생회장",
            "학과"
        )
        val targetAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            targets
        )
        targetAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerTarget.adapter = targetAdapter
    }

    /**
     * ViewModel의 LiveData를 관찰하여 결과 처리
     */
    private fun observeViewModel() {
        // 저장 성공 시
        viewModel.saveSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(
                    this,
                    "템플릿이 저장되었습니다! 💾",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // 저장 후 화면 닫기 (MainActivity로 돌아감)
            }
        })

        // 에러 메시지 표시
        viewModel.errorMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}