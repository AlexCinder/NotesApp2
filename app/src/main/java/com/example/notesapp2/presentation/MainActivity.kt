package com.example.notesapp2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.R
import com.example.notesapp2.databinding.ActivityMainBinding
import com.example.notesapp2.presentation.utils.ItemTouchHelperFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.fabSave.setOnClickListener {
            val intent = NoteActivity.newIntentAdd(this)
            startActivity(intent)
        }
        initRecyclerView()
        viewModel.list.observe(this) {
            noteAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter()
        noteAdapter.onClickListener = {
            val intent = NoteActivity.newIntentEdit(this, it.id)
            startActivity(intent)
            Log.d("TAG", "initRecyclerView: $it ")
        }
        with(binding.rv) {
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