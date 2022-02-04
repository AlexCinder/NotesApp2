package com.example.notesapp2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notesapp2.data.dao.NoteDao
import com.example.notesapp2.data.entity.NoteDbModel

@Database(entities = [NoteDbModel::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun dao(): NoteDao
}