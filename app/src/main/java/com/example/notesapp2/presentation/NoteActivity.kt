package com.example.notesapp2.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp2.R
import com.example.notesapp2.domain.models.Note

class NoteActivity : AppCompatActivity() {

    private var screenMode: String = ACTION_MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", " NoteActivity onCreate: ")
        setContentView(R.layout.edit_note)
        try {
            parseIntent()
        } catch (e: RuntimeException) {
            finish()
        }
        if (savedInstanceState == null) {
            launchFragment()
        }
        Log.d("TAG", "NoteActivity savedInstanceState: ${savedInstanceState?.toString()} ")
    }

    private fun launchFragment() {
        val fragment = when (screenMode) {
            ACTION_MODE_EDIT -> NoteFragment.newInstanceEditFragment(noteId)
            ACTION_MODE_ADD -> NoteFragment.newInstanceAddFragment()
            else -> throw RuntimeException("Unknown mode")
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.note_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (intent.hasExtra(MODE)) {
            screenMode = intent.getStringExtra(MODE) ?: ACTION_MODE_UNKNOWN
            if (screenMode == ACTION_MODE_EDIT) {
                noteId = intent.getLongExtra(ID_FOR_EDIT, Note.UNDEFINED_ID)
            }
        } else throw RuntimeException("Empty intent")
    }

    companion object {

        private const val ACTION_MODE_UNKNOWN = "unknown mode"
        private const val MODE = "mode"
        private const val ACTION_MODE_ADD = "add"
        private const val ACTION_MODE_EDIT = "edit"
        private const val ID_FOR_EDIT = "id"

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(MODE, ACTION_MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, noteId: Long): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(MODE, ACTION_MODE_EDIT)
            intent.putExtra(ID_FOR_EDIT, noteId)
            return intent
        }

    }
//    override fun onStart() {
//        super.onStart()
//        Log.d("TAG", " NoteActivity onStart: ")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("TAG", " NoteActivity onResume: ")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("TAG", " NoteActivity onPause: ")
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("TAG", " NoteActivity onStop: ")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("TAG", " NoteActivity onDestroy: ")
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d("TAG", " NoteActivity onRestart: ")
//    }
}