package com.example.notesapp2.domain.models

data class Note(

    val title: String,
    val description: String,
    val priority: Int,
    var id: Long = UNDEFINED_ID,
    var isPinned: Boolean = false

) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
