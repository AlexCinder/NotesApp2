package com.example.notesapp2.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp2.domain.models.Note
import com.example.notesapp2.domain.repositories.NoteRepository
import com.example.notesapp2.domain.usecases.CreateNoteUseCase
import com.example.notesapp2.domain.usecases.EditNoteUseCase
import com.example.notesapp2.domain.usecases.GetNoteUseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class NoteViewModel(repository: NoteRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val createNoteUseCase = CreateNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note

    private val _visibility = MutableLiveData<Boolean>()
    val visibility: LiveData<Boolean>
        get() = _visibility

    private val _finish = MutableLiveData<Unit>()
    val finish: LiveData<Unit>
        get() = _finish

    fun createNote(
        title: String?,
        description: String?,
        uri: Uri?,
        priority: Int
    ) {
        val noteTitle = parseTitle(title)
        val noteDescription = parseDescription(description)
        val noteUri = parseUri(uri)
        val note = Note(
            title = noteTitle,
            description = noteDescription,
            priority = priority,
            uri = noteUri
        )
        val valid = checkInput(noteTitle, noteDescription)
        if (valid) {
            compositeDisposable.add(Completable.fromAction {
                Log.d("TAG", "Thread:${Thread.currentThread().name} ")
                createNoteUseCase.createNote(note)
            }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Thread:${Thread.currentThread().name} ")
                    finishActivity()
                }, {
                    Log.d("TAG", "createNote: $it")
                }
                ))
        }
    }

    fun editNote(
        title: String?,
        description: String?,
        uri: Uri?,
        priority: Int
    ) {
        val noteTitle = parseTitle(title)
        val noteDescription = parseDescription(description)
        val noteUri = parseUri(uri)
        val valid = checkInput(noteTitle, noteDescription)
        if (valid) {
            _note.value?.let {
                compositeDisposable.add(
                    Completable.fromAction {
                        Action {
                            val note = it.copy(
                                title = noteTitle,
                                description = noteDescription,
                                priority = priority,
                                uri = noteUri,
                            )
                            Log.d("TAG", "inside :${Thread.currentThread().name} ")
                            editNoteUseCase.editNote(note)
                        }.run()
                    }.subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d("TAG", "Thread:${Thread.currentThread().name} ")
                            finishActivity()
                        }, {
                            Log.d("TAG", "editNote:$it ")
                        })
                )
            }
        }
    }

    fun getNote(id: Long,consumer: Consumer<Note>) {
        compositeDisposable.add(
            Single.fromCallable {
                Log.d("TAG", "getNote: ${Thread.currentThread().name}")
               val note = getNoteUseCase.getNote(id)
                _note.postValue(note)
                _visibility.postValue(note.uri.isNotBlank())
                return@fromCallable note
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    consumer.accept(it)
                },{
                })
        )
    }

    private fun parseTitle(title: String?): String {
        return title?.trim() ?: ""
    }

    private fun parseDescription(description: String?): String {
        return description?.trim() ?: ""
    }

    private fun parseUri(uri: Uri?): String {
        return uri?.toString() ?: ""
    }

    private fun checkInput(title: String, description: String): Boolean {
        if (title.isBlank() || description.isBlank()) {

            return false
        }
        return true
    }

    private fun finishActivity() {
        _finish.postValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        Log.d("TAG", "onCleared: $this")
    }

}