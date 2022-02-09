package com.example.notesapp2.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository
import com.example.notesapp2.domain.usecases.DeleteNoteUseCase
import com.example.notesapp2.domain.usecases.EditNoteUseCase
import com.example.notesapp2.domain.usecases.GetNoteListUseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MainViewModel(repository: NoteRepository) : ViewModel() {

    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val composite = CompositeDisposable()
    val list = getNoteListUseCase.getNoteList()

    fun deleteNote(note: Note) {
        composite.add(
            Completable.fromAction {
                deleteNoteUseCase.deleteNoteItem(note)
            }.subscribeOn(Schedulers.io())
                .subscribe({

                },{
                    Log.d("TAG", "deleteNote: $it")
                })
        )
    }

    fun editNote(note: Note) {
        editNoteUseCase.editNote(note)
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
        Log.d("TAG", "onCleared: $this")
    }

}