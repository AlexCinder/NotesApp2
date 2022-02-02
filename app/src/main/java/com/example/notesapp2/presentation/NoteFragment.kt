package com.example.notesapp2.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp2.databinding.FragmentNoteBinding
import com.example.notesapp2.domain.models.Note.Companion.UNDEFINED_ID

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private var screenMode: String = ACTION_MODE_UNKNOWN
    private var noteId = UNDEFINED_ID
    private var uri: Uri? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
            uri = result
            binding.image.setImageURI(uri)
            binding.llImage.visibility = View.VISIBLE
            uri?.let {
                activity?.contentResolver?.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        parseArgs()
        Log.d("TAG", "onCreate: ${savedInstanceState.toString()}")
        super.onCreate(savedInstanceState)
//        Log.d("TAG", " Fragment onCreate: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("TAG", "onSaveInstanceState: ${outState.isEmpty}")
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        Log.d("TAG", " Fragment onCreateView: ")
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", " Fragment onViewCreated: ")
        initClickListeners()
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        when (screenMode) {
            ACTION_MODE_ADD -> launchAddMode()
            ACTION_MODE_EDIT -> launchEditMode()
        }
        viewModel.finish.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
        viewModel.visibility.observe(viewLifecycleOwner) {
            if (it) {
                binding.llImage.visibility = View.VISIBLE
            } else binding.llImage.visibility = View.GONE
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("TAG", "onViewStateRestored: ${savedInstanceState.toString()}")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        Log.d("TAG", " Fragment onDestroyView: ")
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(MODE)) {
            screenMode = args.getString(MODE) ?: ACTION_MODE_UNKNOWN
            if (screenMode == ACTION_MODE_EDIT && args.containsKey(ID_FOR_EDIT)) {
                noteId = args.getLong(ID_FOR_EDIT, UNDEFINED_ID)
            }
        } else throw RuntimeException("Empty arguments")
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
            note.observe(viewLifecycleOwner) {
                binding.apply {
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    image.setImageURI(Uri.parse(it.uri))
                }
                uri = Uri.parse(it.uri)
            }
            with(binding) {
                ibSave.setOnClickListener {
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

    companion object {

        private const val ACTION_MODE_UNKNOWN = "unknown mode"
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val ACTION_MODE_ADD = "add"
        private const val ACTION_MODE_EDIT = "edit"
        private const val ID_FOR_EDIT = "id"
        private const val MODE = "mode"

        fun newInstanceAddFragment(): NoteFragment {
            return NoteFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, ACTION_MODE_ADD)
                }
            }
        }

        fun newInstanceEditFragment(noteId: Long): NoteFragment {
            return NoteFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, ACTION_MODE_EDIT)
                    putLong(ID_FOR_EDIT, noteId)
                }
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("TAG", " Fragment onResume: ")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("TAG", " Fragment onPause: ")
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("TAG", " Fragment onStop: ")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("TAG", " Fragment onDestroy: ")
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Log.d("TAG", " Fragment onAttach: ")
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Log.d("TAG", " Fragment onDetach: ")
//    }

}
