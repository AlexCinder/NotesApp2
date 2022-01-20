package com.example.notesapp2.domain.repositories

import androidx.lifecycle.LiveData
import com.example.notesapp2.domain.models.Note

interface NoteRepository {

    fun createNote(note: Note)

    fun editNote(note: Note)

    fun deleteNote(note: Note)

    fun getNoteList(): LiveData<List<Note>>

    fun getNote(id: Long): Note
}
