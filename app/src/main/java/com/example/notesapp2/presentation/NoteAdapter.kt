package com.example.notesapp2.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.notesapp2.R
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.presentation.utils.NoteDiffCallback
import com.example.notesapp2.presentation.utils.NoteViewHolder

class NoteAdapter :
    ListAdapter<Note, NoteViewHolder>(NoteDiffCallback()) {

    var onClickListener: ((Note) -> Unit)? = null

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

        val note = getItem(position)
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

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if ((item.id % 2) == 0L) {
            EVEN
        } else NOT_EVEN
    }


    companion object {
        const val EVEN = 0
        const val NOT_EVEN = 1
        const val POOL_SIZE = 10
    }
}