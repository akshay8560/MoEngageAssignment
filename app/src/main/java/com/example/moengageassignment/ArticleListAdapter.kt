package com.example.moengageassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ArticleListAdapter(private val onItemClicked: (String) -> Unit) :
    ListAdapter<ArticleData, ArticleListAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.article_single_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitleName: TextView = itemView.findViewById(R.id.tvTitleName)
        private val tvLink: TextView = itemView.findViewById(R.id.tvLink)

        fun bind(item: ArticleData) {
            tvTitleName.text = item.title
            tvLink.text = item.url

            // Set click listener to open the browser
            itemView.setOnClickListener {
                item.url?.let { it1 -> onItemClicked(it1) }
            }
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<ArticleData>() {

    override fun areItemsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
        return oldItem == newItem
    }
}