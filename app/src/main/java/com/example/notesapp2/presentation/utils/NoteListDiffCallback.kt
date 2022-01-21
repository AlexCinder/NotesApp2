package com.example.notesapp2.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.notesapp2.domain.models.Note

class NoteListDiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] == newList[newItemPosition]
    }
}