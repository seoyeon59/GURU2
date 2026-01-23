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

class CommunityAdapter(private var items: List<Template>) :
    RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    fun submit(list: List<Template>) {
        items = list
        notifyDataSetChanged()
    }

    // ViewHolder: item_community.xml의 뷰들을 연결합니다.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // item_community.xml에서 설정한 새로운 ID들로 매칭합니다.
        val tvIndex: TextView = view.findViewById(R.id.tvItemIndex)
        val tvTitle: TextView = view.findViewById(R.id.tvItemTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvItemAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // 번호 표시: 리스트의 순서(position + 1) 혹은 DB의 index를 사용합니다.
        holder.tvIndex.text = (position + 1).toString()

        // 제목 표시
        holder.tvTitle.text = item.title

        // 작성자 표시: null일 경우를 대비해 "익명" 처리를 추가했습니다.
        holder.tvAuthor.text = item.authorId ?: "익명"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {

                putExtra(IntentKeys.TEMPLATE_ID, item.index)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}