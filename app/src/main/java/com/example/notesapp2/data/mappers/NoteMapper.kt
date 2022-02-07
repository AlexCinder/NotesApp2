package com.example.notesapp2.data.mappers

import com.example.notesapp2.data.entity.NoteDbModel
import com.example.notesapp2.domain.models.Note

class NoteMapper {

    fun mapEntityToDbModel(note: Note): NoteDbModel {
        return NoteDbModel(
            id = note.id,
            title = note.title,
            description = note.description,
            uri = note.uri,
            priority = note.priority,
            isPinned = note.isPinned
        )
    }

     fun mapDbModelToEntity(noteDbModel: NoteDbModel): Note {
        return Note(
            id = noteDbModel.id,
            title = noteDbModel.title,
            description = noteDbModel.description,
            uri = noteDbModel.uri,
            priority = noteDbModel.priority,
            isPinned = noteDbModel.isPinned
        )
    }

    fun mapListDbModelToListEntity(list: List<NoteDbModel>) = list.map{
        mapDbModelToEntity(it)
    }
}