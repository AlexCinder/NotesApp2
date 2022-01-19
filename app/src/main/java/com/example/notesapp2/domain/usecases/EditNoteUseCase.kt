package com.example.notesapp2.domain.usecases

import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class EditNoteUseCase(private val noteRepository: NoteRepository) {

    fun editNote(note: Note) {
        noteRepository.editNote(note)
    }
}