package com.task.noteapp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class NoteViewModel (application: Application) : AndroidViewModel(application) {

    // creating variables for all notes list and repository
    val allNotes : LiveData<List<NoteEntity>>
    private val repository : NoteRepo

    // initializing dao, repository and all notes
    init {
        //getting db and calling getNotesDao function
        val dao = NoteDB.getDatabase(application).getNotesDao()
        //initializing repository by passing dao variable to it
        repository = NoteRepo(dao)
        //initializing allNotes with allNotes from repository
        allNotes = repository.allNotes

    }

    // on below line we are creating a new method for deleting a note. In this we are
    // calling a delete method from our repository to delete our note.
    fun deleteNote (note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    // on below line we are creating a new method for updating a note. In this we are
    // calling a update method from our repository to update our note.
    fun updateNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    // on below line we are creating a new method for adding a new note to our database
    // we are calling a method from our repository to add a new note.
    fun addNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.add(note)
    }

    //converting url to image method.
    fun convertImage(imageurl: String, IV: ImageView) {

        //Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        //Once the executor parses the URL
        //and receives the image, handler will load it
        //in the ImageView
        val handler = Handler(Looper.getMainLooper())

        //Initializing the image
        var image: Bitmap?

        //Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageurl).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                   IV.setImageBitmap(image)
                }
            }

            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}