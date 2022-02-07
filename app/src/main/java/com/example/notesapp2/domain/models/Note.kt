package com.example.notesapp2.domain.models

data class Note(

    var id: Long = UNDEFINED_ID,
    val title: String,
    val description: String,
    var priority: Int = 1,
    var uri: String = EMPTY_URI,
    var isPinned: Boolean = false

) {
    companion object {
        const val UNDEFINED_ID = 0L
        const val EMPTY_URI = ""
    }
}
