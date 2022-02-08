package com.example.notesapp2.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.notesapp2.data.entity.NoteDbModel
import io.reactivex.Completable

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_items")
    fun getNoteList(): LiveData<List<NoteDbModel>>

    @Insert(onConflict = REPLACE)
    fun createNote(noteDbModel: NoteDbModel)

    @Query("DELETE FROM note_items WHERE id =:noteDbId")
    fun deleteNote(noteDbId: Long)

    @Query("SELECT * FROM note_items WHERE id =:noteDbId LIMIT 1")
    fun getNote(noteDbId: Long): NoteDbModel

}
