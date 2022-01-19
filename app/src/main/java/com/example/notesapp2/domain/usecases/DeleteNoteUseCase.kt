package com.example.notesapp2.domain.usecases

import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class DeleteNoteUseCase(private val noteRepository: NoteRepository) {

    fun deleteNoteItem(note: Note) {
        noteRepository.deleteNoteItem(note)
    }
}