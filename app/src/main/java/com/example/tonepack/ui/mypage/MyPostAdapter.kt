package com.example.tonepack.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonepack.R
import com.example.tonepack.data.local.entity.Template

class MyPostAdapter(
    private val onItemClick: (Template) -> Unit,
    private val onDeleteClick: (Template) -> Unit
) : ListAdapter<Template, MyPostAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPostTitle: TextView = itemView.findViewById(R.id.tvPostTitle)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(item: Template) {
            tvPostTitle.text = item.title

            itemView.setOnClickListener {
                onItemClick(item)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_my_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Template>() {
            override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
                return oldItem.index == newItem.index
            }

            override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
                return oldItem == newItem
            }
        }
    }
}
