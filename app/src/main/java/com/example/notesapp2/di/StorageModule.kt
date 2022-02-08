package com.example.notesapp2.di

import android.content.Context
import androidx.room.Room
import com.example.notesapp2.data.db.NoteDatabase
import com.example.notesapp2.data.mappers.NoteMapper
import com.example.notesapp2.data.repo.NotesLocalDataSourceImpl

class StorageModule(context: Context) {

    private val mapper = NoteMapper()
    private val database =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            DB_NAME
        ).build()
    private val localDataSourceImpl = NotesLocalDataSourceImpl(database.dao(), mapper)

    fun getLocalDataSourceImpl() = localDataSourceImpl

    companion object {
        private const val DB_NAME = "notes.db"
    }
}