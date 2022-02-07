package com.example.notesapp2.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class NoteRepositoryImpl(
    private val localDataSource: NotesLocalDataSourceImpl
) : NoteRepository {
    override fun createNote(note: Note) {
        localDataSource.createNote(note)
    }

    override fun editNote(note: Note) {
        localDataSource.editNote(note)
    }

    override fun deleteNote(note: Note) {
        localDataSource.deleteNote(note)
    }

    override fun getNoteList(): LiveData<List<Note>> {
        return localDataSource.getNoteList()
    }

    override fun getNote(id: Long): Note {
        return localDataSource.getNote(id)
    }

}