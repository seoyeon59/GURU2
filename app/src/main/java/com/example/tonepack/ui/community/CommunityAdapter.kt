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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle) //tvIndex 선언을 안한 상태이기에 삭제했습니다. 확인 필요합니다.(수민)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvAuthor.text = item.authorId  // authorId가 nullable이면 ?: "익명" 추가

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(IntentKeys.TEMPLATE_ID, item.index) //id가 아닌 무조건 index해야지 오류 발생 안합니다!(수민)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}
