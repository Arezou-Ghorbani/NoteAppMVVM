package com.example.noteappmvvm.data.repository

import com.example.noteappmvvm.data.database.NoteDao
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: NoteDao) {
    fun allNotes() = dao.getAllNotes()
}