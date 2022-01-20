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

class NoteAdapter() :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var list = listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
     var onClickListener : ((Note) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layout = when (viewType) {
            EVEN -> R.layout.note_item_pinned
            NOT_EVEN -> R.layout.note_item_unpinned
            else -> R.layout.note_item_pinned
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = list[position]
        holder.apply {
            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }

            tvTitle.text = note.title
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

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if ((item.id % 2) == 0L) {
            EVEN
        } else NOT_EVEN
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvLastUpdate: TextView = view.findViewById(R.id.tv_last_update)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)

    }

    interface OnNoteClick {

        fun onNoteClick(note: Note)
    }

    companion object {
        const val EVEN = 0
        const val NOT_EVEN = 1
        const val POOL_SIZE = 10
    }
}