package com.example.notesapp2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp2.domain.models.Note

@Entity(tableName = "note_items")
data class NoteDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    var priority: Int = 1,
    var uri: String = Note.EMPTY_URI,
    var isPinned: Boolean = false
) {
}