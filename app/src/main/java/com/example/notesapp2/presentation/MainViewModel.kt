package com.example.notesapp2.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository
import com.example.notesapp2.domain.usecases.DeleteNoteUseCase
import com.example.notesapp2.domain.usecases.EditNoteUseCase
import com.example.notesapp2.domain.usecases.GetNoteListUseCase

class MainViewModel(repository: NoteRepository) : ViewModel() {

    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)

    val list = getNoteListUseCase.getNoteList()

    fun deleteNote(note: Note) {
        deleteNoteUseCase.deleteNoteItem(note)
    }

    fun editNote(note: Note) {
        editNoteUseCase.editNote(note)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "onCleared: $this")
    }

}