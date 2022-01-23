package com.example.notesapp2.domain.usecases

import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class GetNoteUseCase (private val noteRepository: NoteRepository){

    fun getNote(id: Long): Note {
       return noteRepository.getNote(id)
    }
}
