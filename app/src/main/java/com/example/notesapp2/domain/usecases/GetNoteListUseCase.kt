package com.example.notesapp2.domain.usecases

import androidx.lifecycle.LiveData
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

class GetNoteListUseCase(private val noteRepository: NoteRepository) {

    fun getNoteList(): LiveData<List<Note>> {
        return noteRepository.getNoteList()
    }
}