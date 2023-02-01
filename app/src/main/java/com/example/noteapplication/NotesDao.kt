package com.task.noteapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    // creating add function and passing the note inside it
    //if theres data with same id we ignore the conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note : NoteEntity)

    // creating delete function and passing the note inside it
    @Delete
    suspend fun delete(note: NoteEntity)

    //reading our table and using LiveData so that when data is updated it observes and returns updated data
    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    // creating update function and passing the note inside it
    @Update
    suspend fun update(note: NoteEntity)
}