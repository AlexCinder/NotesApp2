package com.example.notesapp2

import android.app.Application
import androidx.room.Room
import com.example.notesapp2.data.db.NoteDatabase

class NoteApplication : Application() {

    lateinit var db : NoteDatabase

    override fun onCreate() {
        super.onCreate()
        createDatabase()
    }

    private fun createDatabase(){
        db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "db"
        ).build()

    }
}