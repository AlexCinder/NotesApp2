package com.example.notesapp2.domain.usecases

import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class CreateNoteUseCase(private val noteRepository: NoteRepository) {

    fun addNoteItem(note: Note) {
        noteRepository.createNote(note)

    }
}