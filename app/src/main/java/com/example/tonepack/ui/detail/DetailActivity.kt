package com.example.tonepack.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tonepack.databinding.ActivityDetailBinding
import com.example.tonepack.navigation.IntentKeys
import com.example.tonepack.util.ClipboardUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    // 1. 민경님이 만든 뷰모델 연결하기!
    // (지금은 빨간 줄이 뜰 수 있어요, 이따 해결법 알려드릴게요!)
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. [협업 포인트] 현주님이 넘겨준 템플릿 ID 받기
        val templateId = intent.getIntExtra(IntentKeys.TEMPLATE_ID, -1)

        if (templateId != -1) {
            // 뷰모델아, 이 ID로 데이터 좀 가져와줘!
            viewModel.loadTemplate(templateId)
        }

        // 3. 뷰모델이 가져온 데이터를 화면에 뿌려주기 (관찰하기)
        lifecycleScope.launch {
            viewModel.template.collectLatest { template ->
                template?.let {
                    binding.tvTitle.text = it.title        // 제목 세팅
                    binding.tvContent.text = it.content    // 내용 세팅
                    binding.tvLikeCount.text = it.likeCount.toString() // 추천수
                }
            }
        }

        // 4. [민경 담당] 복사 버튼 클릭 이벤트 (ClipboardUtil 활용!)
        binding.btnCopy.setOnClickListener {
            val textToCopy = binding.tvContent.text.toString()
            ClipboardUtil.copyToClipboard(this, textToCopy)
            Toast.makeText(this, "클립보드에 복사되었습니다!", Toast.LENGTH_SHORT).show()
        }

        // 5. 추천 버튼 클릭 이벤트
        binding.btnLike.setOnClickListener {
            viewModel.onLikeClick(templateId)
        }
    }
}