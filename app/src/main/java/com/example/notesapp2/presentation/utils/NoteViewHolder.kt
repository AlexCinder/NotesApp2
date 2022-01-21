package com.example.notesapp2.presentation.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.R

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = view.findViewById(R.id.tv_title)
    val tvLastUpdate: TextView = view.findViewById(R.id.tv_last_update)
    val tvDescription: TextView = view.findViewById(R.id.tv_description)

}