package com.example.notesapp2.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.notesapp2.domain.models.Note

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}