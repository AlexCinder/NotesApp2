package com.example.notesapp2.presentation

import android.net.Uri
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
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val createNoteUseCase = CreateNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _finish = MutableLiveData<Unit>()
    val finish: LiveData<Unit>
        get() = _finish

    fun createNote(
        title: String?,
        description: String?,
        uri: Uri?,
        priority: Int = 1
    ) {
        val noteTitle = parseTitle(title)
        val noteDescription = parseDescription(description)
        val noteUri = parseUri(uri)
        val note = Note(noteTitle, noteDescription, priority, uri = noteUri)
        val valid = checkInput(noteTitle, noteDescription)
        if (valid) {
            createNoteUseCase.createNote(note)
            finishActivity()
        }
    }

    fun editNote(
        title: String?,
        description: String?,
        uri: Uri?,
        priority: Int = 1
    ) {
        val noteTitle = parseTitle(title)
        val noteDescription = parseDescription(description)
        val noteUri = parseUri(uri)
        val valid = checkInput(noteTitle, noteDescription)
        if (valid) {
            _note.value?.let {
                val note = it.copy(
                    title = noteTitle,
                    description = noteDescription,
                    uri = noteUri
                )
                editNoteUseCase.editNote(note)
                finishActivity()
            }
        }
    }

    fun getNote(id: Long) {
        val note = getNoteUseCase.getNote(id)
        _note.value = note
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

    private fun finishActivity() {
        _finish.value = Unit
    }

    private fun resetError() {
        _error.value = false
    }
}