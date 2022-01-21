package com.example.notesapp2.domain.models

data class Note(

    val title: String,
    val description: String,
    var priority: Int = 1,
    var id: Long = UNDEFINED_ID,
    var uri: String,
    var isPinned: Boolean = false

) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
