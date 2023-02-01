package com.task.noteapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//creating table
@Entity(tableName = "notesTable")

//specifying columns we need
class NoteEntity (
    @ColumnInfo(name = "title")val noteTitle :String,
    @ColumnInfo(name = "description")val noteDescription :String,
    @ColumnInfo(name = "image")val image :String,
    @ColumnInfo(name = "timestamp")val timeStamp :String,
    @ColumnInfo(name = "tag")val noteTag :String,
    @ColumnInfo(name = "favorite")val isFavorite :Boolean


    )


{
    // specifying primary key and initializing it with 0
    @PrimaryKey(autoGenerate = true)
    var id = 0
}