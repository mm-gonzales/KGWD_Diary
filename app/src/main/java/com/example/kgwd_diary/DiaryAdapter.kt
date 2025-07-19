package com.example.kgwd_diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DiaryAdapter(private val entries: List<DiaryEntry>) :

    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val content: TextView = itemView.findViewById(R.id.tvContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary_entry, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = entries[position]
        holder.title.text = entry.title
        holder.content.text = entry.content
    }

    override fun getItemCount(): Int = entries.size
}
