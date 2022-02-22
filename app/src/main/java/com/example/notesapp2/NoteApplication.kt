package com.example.notesapp2

import android.app.Application
import com.example.notesapp2.di.AppComponent

class NoteApplication : Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = AppComponent(this)
    }
    fun getComponent() = component
}
