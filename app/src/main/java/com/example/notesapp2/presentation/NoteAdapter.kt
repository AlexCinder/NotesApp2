package com.example.notesapp2.presentation

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.R
import com.example.notesapp2.domain.models.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var list = listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item_pinned,
            parent,
            false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = list[position]
        holder.itemView.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ")
        }
        holder.apply {
            tvTitle.text = note.title.toString()
            tvDescription.text = note.description
            tvLastUpdate.text = ""
            when (note.priority) {
                1 -> tvTitle.setTextColor(Color.RED)
                2 -> tvTitle.setTextColor(Color.YELLOW)
                else -> tvTitle.setTextColor(Color.GRAY)
            }
        }
    }

    override fun getItemCount() = list.size

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvLastUpdate: TextView = view.findViewById(R.id.tv_last_update)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)


    }
}