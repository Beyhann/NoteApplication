package com.task.noteapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors


class RecyclerViewAdapter(
    private var allNotes: ArrayList<NoteEntity>,
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickInterface: NoteClickInterface
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    // on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //initializing all our variables which we have added in layout file.
        val noteTitleTV = itemView.findViewById<TextView>(R.id.listview_item_title)!!
        val noteDescTV = itemView.findViewById<TextView>(R.id.listview_item_short_description)!!
        val noteTagIV = itemView.findViewById<ImageView>(R.id.listview_item_tag)!!
        val dateTV = itemView.findViewById<TextView>(R.id.listview_item_date)!!
        val imageIV = itemView.findViewById<ImageView>(R.id.listview_image)!!
        val favImageIV = itemView.findViewById<ImageView>(R.id.favoriteIV)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.custom_listview,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // on below line we are setting data to items of recycler view
        //for when list fragment is first created.
        holder.noteTitleTV.text = allNotes[position].noteTitle
        holder.noteDescTV.text = allNotes[position].noteDescription
        holder.dateTV.text = allNotes[position].timeStamp

        //setting image of note item if available
        val item = allNotes[position].image
        if( item !="")convertImage(holder,position)
        else holder.imageIV.isVisible=false

        //setting favorite icon for each note according to how
        //it was earlier if notes were created before.
        if (allNotes[position].isFavorite){
            holder.favImageIV.setImageResource(R.drawable.ic_favorite)}
        else holder.favImageIV.visibility = View.GONE

        //setting edited tag icon for each note if note was edited.
        if (allNotes[position].noteTag == "Edited"){
         holder.noteTagIV.setImageResource(R.drawable.ic_edited)}
        else holder.noteTagIV.visibility = View.GONE

        // on below line we are adding click listener
        // to our recycler view item.
        holder.itemView.setOnClickListener {
            // on below line we are calling a note click interface
            // and we are passing a position to it.
            noteClickInterface.onNoteClick(allNotes[position])
        }

    }

    //delete note function
    fun delete(position: Int){
        noteClickDeleteInterface.onDeleteIconClick(allNotes[position])

    }

    //overriding items count according to notes array size.
    override fun getItemCount(): Int {
        // on below line we are
        // returning our list size.
        return allNotes.size
    }

    //setting data for search process
    fun setData(arrNotesList: List<NoteEntity>){
        allNotes = arrNotesList as ArrayList<NoteEntity>
    }

    // below method is use to update our list of notes.
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<NoteEntity>) {
        // on below line we are clearing
        // our notes array list
        allNotes.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allNotes.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }

    //converting url to image
    private fun convertImage(holder: ViewHolder, position: Int){

        holder.imageIV.isVisible=true

        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Initializing the image
        var image: Bitmap?

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            //getting image URL
            val imageURL = allNotes[position].image

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    holder.imageIV.setImageBitmap(image)
                }
            }

            // If the URL doesn't point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

interface NoteClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(note: NoteEntity)
}

interface NoteClickInterface {
    // creating a method for click action
    // on recycler view item for updating it.
    fun onNoteClick(note: NoteEntity)
}