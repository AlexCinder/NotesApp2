package com.example.notesapp2.presentation.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.presentation.MainViewModel
import com.example.notesapp2.presentation.NoteAdapter

class ItemTouchHelperFactory(
    val noteAdapter: NoteAdapter,
    val viewModel: MainViewModel
) {
    companion object {
        private const val DRAG_DIRECTION = 0

    }

    fun createItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            DRAG_DIRECTION,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = noteAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteNote(note)
            }
        })
    }

}