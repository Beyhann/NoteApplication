package com.task.noteapp

import androidx.lifecycle.LiveData

class NoteRepo (private val notesDao: NotesDao){

    val allNotes: LiveData<List<NoteEntity>> = notesDao.getAllNotes()

    // creating an add method for deleting  note from db
    suspend fun add (note: NoteEntity){
        notesDao.insert(note)
    }

    // creating a delete method for deleting  note from db
    suspend fun delete(note: NoteEntity){
        notesDao.delete(note)
    }

    // creating an update method for deleting  note from db
    suspend fun update(note: NoteEntity){
        notesDao.update(note)
    }

}