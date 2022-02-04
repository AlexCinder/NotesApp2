package com.example.notesapp2

import android.app.Application
import androidx.room.Room
import com.example.notesapp2.data.db.NoteDatabase

class NoteApplication : Application() {

    val db by lazy {
        Room.databaseBuilder(
            this,
            NoteDatabase::class.java,
            DB_NAME
        ).build()
    }

    companion object {
        private const val DB_NAME = "notes.db"
    }
}
