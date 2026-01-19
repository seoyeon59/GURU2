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

/**
 * 템플릿 상세 화면 Activity
 *
 * 기능:
 *  1. 제목, 내용, 상황/상대 태그 표시
 *  2. 복사 버튼: 템플릿 내용을 클립보드에 복사 (ClipboardUtil 사용)
 *  3. 추천/비추천 버튼: DB의 카운트를 +1 증가
 *
 */
class DetailActivity : AppCompatActivity() {

    // ViewModel 연결 (자동으로 DetailViewModel 인스턴스 생성)
    private val viewModel: DetailViewModel by viewModels()

    // UI 요소들
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvSituation: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvLikeCount: TextView
    private lateinit var tvDislikeCount: TextView
    private lateinit var btnCopy: Button
    private lateinit var btnLike: Button
    private lateinit var btnDislike: Button

    // 현재 보고 있는 템플릿의 ID (Intent로 받아옴)
    private var currentTemplateId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Intent로 전달받은 템플릿 ID 가져오기
        currentTemplateId = intent.getIntExtra("TEMPLATE_ID", -1)

        if (currentTemplateId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. XML의 뷰들을 코드와 연결
        initViews()

        // 3. ViewModel의 데이터 변화를 감지하는 관찰자 설정
        observeViewModel()

        // 4. 버튼 클릭 이벤트 설정
        setupListeners()

        // 5. 데이터 로딩 시작 (ViewModel에게 요청)
        viewModel.loadTemplate(currentTemplateId)
    }

    /**
     * XML의 View들을 findViewById로 연결
     */
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

    /**
     * ViewModel의 LiveData를 관찰하여 UI 업데이트
     * 데이터가 변경될 때마다 자동으로 화면이 갱신됨
     */
    private fun observeViewModel() {
        // 템플릿 데이터 관찰
        viewModel.template.observe(this, Observer { template ->
            template?.let {
                // 서연님의 Template 엔티티 필드 사용
                tvTitle.text = it.title
                tvContent.text = it.content
                tvSituation.text = "상황: ${it.situation}"
                tvTarget.text = "상대: ${it.target}"
                tvLikeCount.text = "👍 ${it.likeCount}"
                tvDislikeCount.text = "👎 ${it.dislikeCount}"
            }
        })

        // 로딩 상태 관찰 (필요시 ProgressBar 표시)
        viewModel.isLoading.observe(this, Observer { isLoading ->
            // TODO: 로딩 중일 때 UI 처리 (예: ProgressBar)
        })
    }

    /**
     * 버튼 클릭 이벤트 설정
     */
    private fun setupListeners() {
        // 서연님의 ClipboardUtil.copyToClipboard() 사용
        btnCopy.setOnClickListener {
            val content = viewModel.template.value?.content ?: ""
            if (content.isNotEmpty()) {
                ClipboardUtil.copyToClipboard(this, content)
                Toast.makeText(this, "클립보드에 복사되었습니다 📋", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "복사할 내용이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // ViewModel을 통해 DB 업데이트 후 화면 갱신
        btnLike.setOnClickListener {
            viewModel.onLikeClicked(currentTemplateId)
            Toast.makeText(this, "추천했습니다! 👍", Toast.LENGTH_SHORT).show()
        }

        // [민경 담당] 비추천 버튼 클릭
        btnDislike.setOnClickListener {
            viewModel.onDislikeClicked(currentTemplateId)
            Toast.makeText(this, "비추천했습니다 👎", Toast.LENGTH_SHORT).show()
        }
    }
}