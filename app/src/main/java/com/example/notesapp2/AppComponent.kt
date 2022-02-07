package com.example.notesapp2

import android.app.Application
import androidx.room.Room
import com.example.notesapp2.data.db.NoteDatabase
import com.example.notesapp2.data.mappers.NoteMapper
import com.example.notesapp2.data.repo.NoteRepositoryImpl
import com.example.notesapp2.data.repo.NotesLocalDataSourceImpl

class AppComponent(application: Application) {

    private val mapper = NoteMapper()
    private val database =
        Room.databaseBuilder(
            application.applicationContext,
            NoteDatabase::class.java,
            DB_NAME
        ).allowMainThreadQueries()
            .build()

    private val dao = database.dao()
    private val localDataSourceImpl = NotesLocalDataSourceImpl(dao, mapper)
    private val repository = NoteRepositoryImpl(localDataSourceImpl)

    fun getRepository() = repository

    companion object {
        private const val DB_NAME = "notes.db"
    }
}