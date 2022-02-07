package com.example.notesapp2.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp2.domain.repositories.NoteRepository
import com.example.notesapp2.presentation.MainViewModel
import com.example.notesapp2.presentation.NoteViewModel

class ViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NoteViewModel::class.java) -> NoteViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            else -> throw RuntimeException("unknown type of viewModel")
        }
    }
}
