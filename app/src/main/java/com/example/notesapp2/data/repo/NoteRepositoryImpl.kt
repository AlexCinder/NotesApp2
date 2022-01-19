package com.example.notesapp2.data.repo

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository

object NoteRepositoryImpl : NoteRepository {
    private val noteListLiveData = MutableLiveData<List<Note>>()
    private val noteList = sortedSetOf<Note>({ o2, o1 ->
        o1.id.compareTo(o2.id)
    })
    private var autoId = 0L


    override fun createNote(note: Note) {
        if (note.id == Note.UNDEFINED_ID) {
            note.id = autoId++
        }
        noteList.add(note)
        updateList()
    }

    override fun editNote(note: Note) {
        val oldNote = getNoteItem(note.id)
        noteList.remove(oldNote)
        createNote(note)
    }

    override fun deleteNoteItem(note: Note) {
        noteList.remove(note)
        updateList()
    }

    override fun getNoteList(): LiveData<List<Note>> {
        return noteListLiveData
    }

    override fun getNoteItem(id: Long): Note {
        return noteList.find {
            it.id == id
        }?: throw RuntimeException("Can't find note with id $id")
    }

    private fun updateList(){
        noteListLiveData.value = noteList.toList()
    }
}