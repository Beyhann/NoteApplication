package com.task.noteapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(NoteEntity::class), version = 3, exportSchema = false)
abstract class NoteDB : RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    // using Singleton to prevent multiple of instances of database opening at the same time.
    companion object {


        //creating instance of db
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context): NoteDB {
            // if the INSTANCE is not null return it, if null create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}