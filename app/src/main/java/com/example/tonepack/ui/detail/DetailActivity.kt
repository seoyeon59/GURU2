package com.example.tonepack.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.tonepack.R
import com.example.tonepack.util.ClipboardUtil

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvSituation: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvLikeCount: TextView
    private lateinit var tvDislikeCount: TextView
    private lateinit var btnCopy: Button
    private lateinit var btnLike: Button
    private lateinit var btnDislike: Button

    private var currentTemplateId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        currentTemplateId = intent.getIntExtra("TEMPLATE_ID", -1)

        if (currentTemplateId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        observeViewModel()
        setupListeners()

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

    // LiveData 관찰을 통한 UI 자동 업데이트
    private fun observeViewModel() {
        viewModel.template.observe(this, Observer { template ->
            template?.let {
                tvTitle.text = it.title
                tvContent.text = it.content
                tvSituation.text = "상황: ${it.situation}"
                tvTarget.text = "상대: ${it.target}"
                tvLikeCount.text = "👍 ${it.likeCount}"
                tvDislikeCount.text = "👎 ${it.dislikeCount}"
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            // 로딩 상태에 따른 UI 처리 필요 시 작성
        })
    }

    private fun setupListeners() {
        // 클립보드 복사 기능
        btnCopy.setOnClickListener {
            val content = viewModel.template.value?.content ?: ""
            if (content.isNotEmpty()) {
                ClipboardUtil.copyToClipboard(this, content)
                Toast.makeText(this, "클립보드에 복사되었습니다 📋", Toast.LENGTH_SHORT).show()
            }
        }

        // 추천 클릭 이벤트
        btnLike.setOnClickListener {
            viewModel.onLikeClicked(currentTemplateId)
            Toast.makeText(this, "추천했습니다! 👍", Toast.LENGTH_SHORT).show()
        }

        // 비추천 클릭 이벤트
        btnDislike.setOnClickListener {
            viewModel.onDislikeClicked(currentTemplateId)
            Toast.makeText(this, "비추천했습니다 👎", Toast.LENGTH_SHORT).show()
        }
    }
}