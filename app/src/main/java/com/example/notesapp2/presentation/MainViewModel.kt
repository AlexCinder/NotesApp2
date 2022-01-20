package com.example.notesapp2.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.notesapp2.data.repo.NoteRepositoryImpl
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.usecases.DeleteNoteUseCase
import com.example.notesapp2.domain.usecases.EditNoteUseCase
import com.example.notesapp2.domain.usecases.GetNoteListUseCase

class MainViewModel() : ViewModel() {

    private val repository = NoteRepositoryImpl
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


}