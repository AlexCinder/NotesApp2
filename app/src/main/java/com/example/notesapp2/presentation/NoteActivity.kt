package com.example.notesapp2.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp2.R
import com.example.notesapp2.databinding.EditNoteBinding
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.models.Note.Companion.EMPTY_URI

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: EditNoteBinding
    private lateinit var viewModel: NoteViewModel
    private var screenMode: String = ACTION_MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID
    private lateinit var searchImage: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            parseIntent()
        } catch (e: RuntimeException) {
            finish()
        }
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        createBottomNavigationView()
        when (screenMode) {
            ACTION_MODE_ADD -> launchAddMode()
            ACTION_MODE_EDIT -> launchEditMode()
        }


    }

    private fun launchAddMode() {
        viewModel.createNote(
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            uri
        )

    }

    private fun launchEditMode() {
        with(viewModel) {
            getNote(noteId)
            note.observe(this@NoteActivity) {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
                binding.image.setImageURI(Uri.parse(it.uri))
                //TODO(add priority and pinned features)
            }
        }


    }

    private fun parseIntent() {
        if (intent.hasExtra(ACTIVITY_MODE)) {
            screenMode = intent.getStringExtra(ACTIVITY_MODE) ?: ACTION_MODE_UNKNOWN
            if (screenMode == ACTION_MODE_EDIT) {
                noteId = intent.getLongExtra(ID_FOR_EDIT, Note.UNDEFINED_ID)
            }
        } else throw RuntimeException("Empty intent")

    }

    private fun createBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                LIST_ITEM -> {
                    Toast.makeText(this, "list item", LENGTH_LONG).show()
                    true
                }
                CAMERA_ITEM -> {
                    Toast.makeText(this, "camera item", LENGTH_LONG).show()
                    true
                }
                SAVE_ITEM -> {
                    viewModel.finish.observe(this) {
                        this@NoteActivity.finish()
                    }
                    true
                }
                LOCATION_ITEM -> {
                    Toast.makeText(this, "location item", LENGTH_LONG).show()
                    true
                }
                else -> false

            }
        }
    }


    companion object {
        private const val ACTION_MODE_UNKNOWN = "unknown mode"
        private const val CAMERA_ITEM = R.id.camera
        private const val SAVE_ITEM = R.id.save
        private const val LOCATION_ITEM = R.id.location
        private const val LIST_ITEM = R.id.list
        private const val ACTIVITY_MODE = "activity mode"
        private const val ACTION_MODE_ADD = "add"
        private const val ACTION_MODE_EDIT = "edit"
        private const val ID_FOR_EDIT = "id"

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, ACTION_MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, noteId: Long): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, ACTION_MODE_EDIT)
            intent.putExtra(ID_FOR_EDIT, noteId)
            return intent
        }

        private fun newIntentAddImage(): Intent {
            return Any() as Intent
        }

    }
}