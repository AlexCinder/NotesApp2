package com.example.notesapp2

import android.app.Application
import androidx.room.Room
import com.example.notesapp2.data.db.NoteDatabase

class NoteApplication : Application() {

    private lateinit var _db: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        createDatabase()
    }

    fun getDataBase(): NoteDatabase {
        return _db
    }

    private fun createDatabase() {
        _db = Room.databaseBuilder(
            this,
            NoteDatabase::class.java,
            DB_NAME
        ).build()
    }

    companion object {
        private const val DB_NAME = "notes.db"
    }
}
