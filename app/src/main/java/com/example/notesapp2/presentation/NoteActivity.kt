package com.example.notesapp2.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.notesapp2.R
import com.example.notesapp2.databinding.EditNoteBinding
import com.example.notesapp2.domain.models.Note

class NoteActivity : AppCompatActivity() {

    private val binding by lazy { EditNoteBinding.inflate(layoutInflater) }
    private lateinit var viewModel: NoteViewModel
    private var screenMode: String = ACTION_MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID
    private var uri: Uri? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
            uri = result
            Log.d("TAG", "${uri?.toString()}")
            binding.image.setImageURI(uri)
            binding.llImage.visibility = View.VISIBLE
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try {
            parseIntent()
        } catch (e: RuntimeException) {
            finish()
        }
        initClickListeners()
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        when (screenMode) {
            ACTION_MODE_ADD -> launchAddMode()
            ACTION_MODE_EDIT -> launchEditMode()
        }
        viewModel.finish.observe(this) {
            finish()
        }
        viewModel.visibility.observe(this) {
            if (it) {
                binding.llImage.visibility = View.VISIBLE
            } else binding.llImage.visibility = View.GONE
        }


    }

    private fun launchAddMode() {
        with(binding) {
            ibSave.setOnClickListener {
                viewModel.createNote(
                    etTitle.text?.toString(),
                    etDescription.text?.toString(),
                    uri = uri
                )
            }
        }
    }

    private fun launchEditMode() {
        with(viewModel) {
            getNote(noteId)
            note.observe(this@NoteActivity) {
                binding.apply {
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    image.setImageURI(Uri.parse(it.uri))
                }
                uri = Uri.parse(it.uri)
            }
            with(binding) {
                ibSave.setOnClickListener {
                    Log.d("TAG", "launchEditMode: $uri")
                    editNote(
                        etTitle.text?.toString(),
                        etDescription.text?.toString(),
                        uri = uri
                    )
                }
            }

        }
    }

    private fun initClickListeners() {
        with(binding) {
            ibCamera.setOnClickListener {
                getContent.launch(arrayOf(MIME_TYPE_IMAGE))
            }
            image.setOnLongClickListener {
                image.setImageURI(null)
                llImage.visibility = View.GONE
                uri = null
                true
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


    private fun addImage() {

    }


    companion object {
        private const val ACTION_MODE_UNKNOWN = "unknown mode"
        private const val MIME_TYPE_IMAGE = "image/*"
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


    }
}