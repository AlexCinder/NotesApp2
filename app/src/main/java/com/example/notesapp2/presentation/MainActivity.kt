package com.example.notesapp2.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.notesapp2.NoteApplication
import com.example.notesapp2.R
import com.example.notesapp2.databinding.ActivityMainBinding
import com.example.notesapp2.presentation.utils.ItemTouchHelperFactory
import com.example.notesapp2.presentation.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModelFactory
            by lazy { ViewModelFactory((application as NoteApplication)
                .getComponent().getRepository()) }
    private val viewModel
            by lazy { ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java] }

    private val noteAdapter: NoteAdapter by lazy { NoteAdapter() }
    private lateinit var binding: ActivityMainBinding
    private var noteContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TAG", " MainActivity onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteContainer = findViewById(R.id.note_item_container)
        initRecyclerView()
        binding.fabAdd.setOnClickListener {
            if (singleMode()) {
                val intent = NoteActivity.newIntentAdd(this)
                startActivity(intent)
            } else launchFragment(NoteFragment.newInstanceAddFragment())
        }
        viewModel.list.observe(this) {
            noteAdapter.submitList(it)
        }
    }

    private fun singleMode(): Boolean {
        return noteContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.note_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        noteAdapter.onClickListener = {
            if (singleMode()) {
                val intent = NoteActivity.newIntentEdit(this, it.id)
                startActivity(intent)
            } else launchFragment(NoteFragment.newInstanceEditFragment(it.id))
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


//    override fun onStart() {
//        super.onStart()
//        Log.d("TAG", " MainActivity onStart: ")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("TAG", " MainActivity onResume: ")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("TAG", " MainActivity onPause: ")
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("TAG", " MainActivity onStop: ")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("TAG", " MainActivity onDestroy: ")
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d("TAG", " MainActivity onRestart: ")
//    }


}