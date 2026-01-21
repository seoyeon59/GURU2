package com.example.tonepack.ui.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tonepack.R
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.ui.detail.DetailActivity
import com.example.tonepack.navigation.IntentKeys

class CommunityAdapter(private val items: List<Template>) :
    RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    // 레이아웃 내 View들을 전달받아 보관
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIndex: TextView = view.findViewById(R.id.tvIndex)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
    }

    // 아이템 레이아웃(XML) 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    // 데이터 연결 및 클릭 이벤트 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvIndex.text = (position + 1).toString()
        holder.tvTitle.text = item.title
        holder.tvAuthor.text = item.authorId ?: "익명"

        // 리스트 항목 클릭 시 상세 화면(DetailActivity)으로 이동
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                // MyPage와 동일한 키로 통일
                putExtra(IntentKeys.TEMPLATE_ID, item.index)
            }
            context.startActivity(intent)
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount() = items.size
}