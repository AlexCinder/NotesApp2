package com.example.notesapp2.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp2.data.repo.NoteRepositoryImpl
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.usecases.CreateNoteUseCase
import com.example.notesapp2.domain.usecases.EditNoteUseCase
import com.example.notesapp2.domain.usecases.GetNoteUseCase

class NoteViewModel : ViewModel() {

    private val repository = NoteRepositoryImpl
    private val editNote = EditNoteUseCase(repository)
    private val createNote = CreateNoteUseCase(repository)
    private val getNote = GetNoteUseCase(repository)
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    fun createNote(
        title: String?,
        description: String?,
        uri: Uri?,
        priority: Int
    ) {
        val noteTitle = parseTitle(title)
        val noteDescription = parseDescription(description)
        val noteUri = parseUri(uri)
        val note = Note(noteTitle, noteDescription, priority, uri = noteUri)
        val valid = checkInput(noteTitle, noteDescription)
        if (valid) {
            createNote.addNoteItem(note)
        }
    }

    fun editNote(note: Note) {
        editNote.editNote(note)
    }

    fun getNote(id: Long) {
        val note = getNote.getNoteItem(id)
    }

    private fun parseTitle(title: String?): String {
        return title?.trim() ?: ""
    }

    private fun parseDescription(description: String?): String {
        return description?.trim() ?: ""
    }

    private fun parseUri(uri: Uri?): String {
        return uri?.toString() ?: ""
    }

    private fun checkInput(title: String, description: String): Boolean {
        if (title.isBlank() || description.isBlank()) {
            _error.value = true
            return false
        }
        return true
    }


}