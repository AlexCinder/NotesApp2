package com.example.notesapp2.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.notesapp2.NoteApplication
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentNoteBinding
import com.example.notesapp2.domain.models.Note.Companion.UNDEFINED_ID
import com.example.notesapp2.presentation.utils.ViewModelFactory

class NoteFragment : Fragment() {

    private lateinit var menu: PopupMenu
    private var priority = NONE
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModelFactory by lazy {
        ViewModelFactory(
            (requireActivity().application as NoteApplication)
                .getComponent().getRepository()
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[NoteViewModel::class.java]
    }
    private var screenMode: String = ACTION_MODE_UNKNOWN
    private var noteId = UNDEFINED_ID
    private var uri: Uri? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
            uri = result
            loadImage(uri.toString())
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
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        initClickListeners()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    uri = uri,
                    priority = priority
                )
            }
        }
    }

    private fun launchEditMode() {

        with(viewModel) {
            getNote(noteId) {
                Log.d("TAG", "getNote: ${Thread.currentThread().name}")
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
                loadImage(it.uri)
                uri = Uri.parse(it.uri)
                priority = it.priority
            }
            with(binding) {
                ibSave.setOnClickListener {
                    editNote(
                        etTitle.text?.toString(),
                        etDescription.text?.toString(),
                        uri = uri,
                        priority = priority
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
            ibLocation.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.note_item_container, MapsFragment())
                    .commit()
            }
            ibChoice.setOnClickListener {
                menu.show()
            }
        }
    }

    private fun loadImage(url:String){
        Glide
            .with(this)
            .load(url)
            .centerCrop()
            .into(binding.image)
    }

    private fun initMenu() {
        menu = PopupMenu(this.requireContext(), binding.ibChoice)
        menu.inflate(R.menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.low_priority -> {
                    priority = 3
                    true
                }
                R.id.medium_priority -> {
                    priority = 2
                    true
                }
                R.id.high_priority -> {
                    priority = 1
                    true
                }
                else -> false
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
        private const val NONE = 0

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
