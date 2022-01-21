package com.example.notesapp2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notesapp2.R

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note)
    }
}