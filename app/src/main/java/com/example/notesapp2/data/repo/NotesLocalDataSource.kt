package com.example.notesapp2.data.repo

import androidx.lifecycle.LiveData
import com.example.notesapp2.domain.models.Note

interface NotesLocalDataSource {

    fun createNote(note: Note)

    fun editNote(note: Note)

    fun deleteNote(note: Note)

    fun getNoteList(): LiveData<List<Note>>

    fun getNote(id: Long): Note

}