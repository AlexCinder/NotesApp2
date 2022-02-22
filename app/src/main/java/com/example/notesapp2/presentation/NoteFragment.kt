package com.example.notesapp2.presentation

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.notesapp2.NoteApplication
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentNoteBinding
import com.example.notesapp2.domain.models.Note.Companion.UNDEFINED_ID
import com.example.notesapp2.presentation.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class NoteFragment : Fragment() {

    private val requestPermission =
        registerForActivityResult(
            RequestPermission(),
            ::onGotLocationsPermissions
        )
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
    private val getImageFromLibrary =
        registerForActivityResult(GetContent(), ::pickImage)

    override fun onCreate(savedInstanceState: Bundle?) {
        parseArgs()
        if (noteId != UNDEFINED_ID) {
            viewModel.getNote(noteId)
        }
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
            note.observe(viewLifecycleOwner) {
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

    private fun onGotLocationsPermissions(granted: Boolean) {
        if (granted) {
//            getCurrentLocation()
            launchMap()
        }
    }

    private fun pickImage(result: Uri?) {
        if (result != null) {
            uri = result
            loadImageToStorage(result,UUID.randomUUID().toString())
//            loadImage(uri.toString())
            binding.llImage.visibility = View.VISIBLE
//            uri?.let {
//                requireActivity().contentResolver.takePersistableUriPermission(
//                    it,
//                    FLAG_GRANT_READ_URI_PERMISSION
//                )
//            }
        }
    }

    private fun launchMap() {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.note_item_container, MapsFragment())
            .commit()
    }

    private fun loadImageToStorage(uri: Uri, fileName: String) {
        val bitmap = getBitmapFromUri(uri)
        var fos: FileOutputStream? = null
        try {
            if (bitmap == null) {
                Toast.makeText(requireActivity(), "Can't save image", Toast.LENGTH_SHORT)
                    .show()
                return
            } else {
                fos = context?.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE)
                fos.use {
                    (bitmap.compress(Bitmap.CompressFormat.JPEG, 60, it))
                }
            }
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "FileNotFoundException: $e")
        } catch (e: IOException) {
            Log.d(TAG, "IOException:$e")
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                Log.d(TAG, "IOException:$e ")
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val resolver = requireContext().contentResolver
        var bitmap: Bitmap? = null
        try {
            val decoder = ImageDecoder.createSource(resolver, uri)
            bitmap = ImageDecoder.decodeBitmap(decoder)
        } catch (e: Exception) {
            Log.d(TAG, "Exception: $e")
        }
        return bitmap
    }

    private fun initClickListeners() {
        with(binding) {
            ibCamera.setOnClickListener {
                getImageFromLibrary.launch(MIME_TYPE_IMAGE)
            }
            image.setOnLongClickListener {
                image.setImageURI(null)
                llImage.visibility = View.GONE
                uri = null
                true
            }
            ibLocation.setOnClickListener {
                checkPermissions()
            }
            ibChoice.setOnClickListener {
                menu.show()
            }
        }
    }

    private fun loadImage(url: String) {
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

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED -> {
//                getCurrentLocation()
                launchMap()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                showDialog()
            }
            else -> {
                requestPermission.launch(ACCESS_FINE_LOCATION)
            }
        }

    }

    private fun showDialog() {
        Snackbar
            .make(
                requireActivity()
                    .findViewById(R.id.ll_bottom),
                "Cannot access to location",
                Snackbar.LENGTH_LONG,
            )
            .setBackgroundTint(Color.BLACK)
            .setTextColor(Color.WHITE)
            .setAction("Go to settings") {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireActivity().packageName, null)
                )
                if (requireActivity().packageManager.resolveActivity(
                        intent, MATCH_DEFAULT_ONLY
                    ) != null
                ) {
                    startActivity(intent)
                }
            }
            .setActionTextColor(Color.WHITE)
            .show()
    }


    companion object {
        private const val IMAGE_DIR = "image"
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
