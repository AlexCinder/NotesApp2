package com.example.notesapp2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.R
import com.example.notesapp2.presentation.utils.ItemTouchHelperFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.list.observe(this) {
            noteAdapter.list = it
        }
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        noteAdapter = NoteAdapter()
        noteAdapter.onClickListener = {
        }
        with(recyclerView) {
            adapter = noteAdapter
            recycledViewPool.setMaxRecycledViews(
                NoteAdapter.EVEN,
                NoteAdapter.POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                NoteAdapter.NOT_EVEN,
                NoteAdapter.POOL_SIZE
            )
            createItemTouchHelper().attachToRecyclerView(this)
        }

    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelperFactory(noteAdapter, viewModel)
            .createItemTouchHelper()
    }
}