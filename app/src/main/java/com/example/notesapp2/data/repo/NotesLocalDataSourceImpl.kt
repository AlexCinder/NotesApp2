package com.example.notesapp2.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.notesapp2.data.db.dao.NoteDao
import com.example.notesapp2.data.mappers.NoteMapper
import com.example.notesapp2.domain.models.Note

class NotesLocalDataSourceImpl(
    private val noteDao: NoteDao,
    private val mapper: NoteMapper
) : NotesLocalDataSource {
    override fun createNote(note: Note) {
        noteDao.createNote(
            mapper.mapEntityToDbModel(note)
        )
    }

    override fun editNote(note: Note) {
        noteDao.createNote(
            mapper.mapEntityToDbModel(note)
        )
    }

    override fun deleteNote(note: Note) {
        noteDao.deleteNote(
            note.id
        )
    }

    override fun getNoteList(): LiveData<List<Note>> {
        return Transformations.map(noteDao.getNoteList()) {
            mapper.mapListDbModelToListEntity(it)
        }
    }

    override fun getNote(id: Long): Note {
        val dbModel = noteDao.getNote(id)
        return mapper.mapDbModelToEntity(dbModel)
    }
}