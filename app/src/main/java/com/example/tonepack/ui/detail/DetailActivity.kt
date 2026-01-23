package com.example.tonepack.ui.detail

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tonepack.App
import com.example.tonepack.R
import com.example.tonepack.util.ClipboardUtil
import com.example.tonepack.navigation.IntentKeys

class DetailActivity : AppCompatActivity() {

    // ViewModel 초기화: Factory에 templateRepository와 sessionManager를 함께 전달합니다.
    private val viewModel: DetailViewModel by viewModels {
        val app = application as App
        DetailViewModelFactory(app.templateRepository, app.sessionManager)
    }

    // UI 컴포넌트 변수
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvSituation: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvLikeCount: TextView
    private lateinit var tvDislikeCount: TextView

    private lateinit var btnCopy: ImageButton
    private lateinit var btnLike: ImageButton
    private lateinit var btnDislike: ImageButton

    private var currentTemplateId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 전달받은 ID 읽기
        currentTemplateId = intent.getIntExtra(IntentKeys.TEMPLATE_ID, -1)

        if (currentTemplateId == -1) {
            Toast.makeText(this, "데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()       // 뷰 연결
        observeViewModel() // 데이터 관찰
        setupListeners()   // 버튼 클릭 설정

        // 데이터 로드 시작
        viewModel.loadTemplate(currentTemplateId)
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tvDetailTitle)
        tvContent = findViewById(R.id.tvDetailContent)
        tvSituation = findViewById(R.id.tvDetailSituation)
        tvTarget = findViewById(R.id.tvDetailTarget)
        tvLikeCount = findViewById(R.id.tvLikeCount)
        tvDislikeCount = findViewById(R.id.tvDislikeCount)

        btnCopy = findViewById(R.id.btnCopy)
        btnLike = findViewById(R.id.btnLike)
        btnDislike = findViewById(R.id.btnDislike)
    }

    // UI 갱신 및 메시지 관찰
    private fun observeViewModel() {
        // 템플릿 데이터 업데이트 관찰
        viewModel.template.observe(this) { template ->
            template?.let {
                tvTitle.text = it.title
                tvContent.text = it.content
                tvSituation.text = "상황: ${it.situation}"
                tvTarget.text = "상대: ${it.target}"
                tvLikeCount.text = it.likeCount.toString()
                tvDislikeCount.text = it.dislikeCount.toString()
            }
        }

        // 추천/비추천 처리 결과 메시지 관찰 (Toast 띄우기)
        viewModel.toastMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 클릭 리스너 설정
    private fun setupListeners() {
        // 복사 기능
        btnCopy.setOnClickListener {
            val content = viewModel.template.value?.content ?: ""
            if (content.isNotEmpty()) {
                ClipboardUtil.copyToClipboard(this, content)
                Toast.makeText(this, "클립보드에 복사되었습니다 📋", Toast.LENGTH_SHORT).show()
            }
        }

        // 추천 기능
        btnLike.setOnClickListener {
            viewModel.onLikeClicked(currentTemplateId)
        }

        // 비추천 기능
        btnDislike.setOnClickListener {
            viewModel.onDislikeClicked(currentTemplateId)
        }
    }
}