package com.example.kgwd_diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(
    private val diaryList: List<DiaryEntry>,
    private val onDeleteClick: (DiaryEntry) -> Unit,
    private val onItemClick: (DiaryEntry) -> Unit // NEW: handle click for editing
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    inner class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val content: TextView = view.findViewById(R.id.tvContent)
        val deleteBtn: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_entry, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = diaryList[position]
        holder.title.text = entry.title
        holder.content.text = entry.content
        holder.deleteBtn.visibility = View.GONE

        // Show delete on long press
        holder.itemView.setOnLongClickListener {
            holder.deleteBtn.visibility = View.VISIBLE
            true
        }

        // Call the delete function
        holder.deleteBtn.setOnClickListener {
            onDeleteClick(entry)
        }

        // NEW: Navigate to edit on regular click
        holder.itemView.setOnClickListener {
            onItemClick(entry)
        }
    }

    override fun getItemCount() = diaryList.size
}


