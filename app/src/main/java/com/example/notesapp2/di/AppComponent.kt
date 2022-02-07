package com.example.notesapp2.di

import android.content.Context
import com.example.notesapp2.data.repo.NoteRepositoryImpl

class AppComponent(context: Context) {

    private val localDataSourceImpl =
        StorageModule(context).getLocalDataSourceImpl()
    private val repository = NoteRepositoryImpl(localDataSourceImpl)

    fun getRepository() = repository

    companion object {
        private const val DB_NAME = "notes.db"
    }
}