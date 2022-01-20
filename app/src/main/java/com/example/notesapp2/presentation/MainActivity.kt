package com.example.notesapp2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.list.observe(this) {
            Log.d("TAG", "onCreate: ${it.size}")
            adapter.list = it
            adapter.notifyDataSetChanged()
            Log.d("TAG", "adapter: ${adapter.itemCount}")
        }
        Log.d("TAG", "list size ${adapter.itemCount}")

    }

    private fun init() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        adapter = NoteAdapter()
        recyclerView.adapter = adapter
    }
}