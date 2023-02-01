package com.task.noteapp


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.task.noteapp.databinding.FragmentNotesListBinding
import java.util.*


// TODO: Rename parameter arguments, choose names that match

class NotesListFragment : Fragment(), NoteClickDeleteInterface, NoteClickInterface {

    //declaring variables as late init so we can use them in functions too.
    private lateinit var binding : FragmentNotesListBinding
    private lateinit var notesRV: RecyclerView
    private lateinit var viewmodel: NoteViewModel
    lateinit var allNotes: ArrayList<NoteEntity>
    lateinit var noteRVAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //hiding action bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_notes_list, container, false)

        //initializing variables
        notesRV = binding.notesRV
        allNotes = ArrayList()

        // setting layout manager to our recycler view.
        notesRV.layoutManager = LinearLayoutManager(context)

        //initializing our adapter class.
         noteRVAdapter = RecyclerViewAdapter(allNotes, this, this)

        //setting adapter to our recycler view.
        notesRV.adapter = noteRVAdapter

        // initializing view model
        viewmodel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        // on below line we are calling all notes method
        // from our view model class to observer the changes on list.
        viewmodel.allNotes.observe(viewLifecycleOwner) { list ->
            list?.let {
                // on below line we are updating our list.
                noteRVAdapter.updateList(it)
            }
        }

        // adding a click listener for fab button
        // and navigating to add new note in EditNoteFragment.
        binding.idFAB.setOnClickListener {
            findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToEditNoteFragment("","",-1,"",false,"New"))
        }


        //slide to delete note
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // this method is called when we swipe our item to right or left directions.
                // on below line we are getting the item at a particular position.
                val deletedNote: NoteEntity =
                    allNotes[viewHolder.adapterPosition]

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                //calling function to delete the note from view model and db
                noteRVAdapter.delete(position)

                //adding undo delete note option
                //below line is to display our snack bar with action.
               Snackbar.make(notesRV, "Deleted " + deletedNote.noteTitle, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        allNotes.add(position, deletedNote)
                        viewmodel.addNote(deletedNote)

                        // below line is to notify item is
                        // added to our adapter class.
                        noteRVAdapter.notifyItemInserted(position)
                    }.show()
            }

            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(notesRV)



        //adding on query listener for our search view.
        binding.idSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(p0: String?): Boolean {

                val tempArr = ArrayList<NoteEntity>()

                for (arr in allNotes){
                    if (arr.noteTitle.lowercase(Locale.getDefault()).contains(p0.toString()) || arr.noteDescription.lowercase(
                            Locale.getDefault()
                        )
                            .contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                noteRVAdapter.setData(tempArr)
                noteRVAdapter.notifyDataSetChanged()
                return true
            }

        })


        return binding.root
    }

    //deleting the note
    override fun onDeleteIconClick(note: NoteEntity) {
        viewmodel.deleteNote(note)
    }

    //navigating to EditNoteFragment When note item is clicked
    override fun onNoteClick(note: NoteEntity) {

        findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToEditNoteFragment(note.noteTitle,note.noteDescription,note.id,note.image,note.isFavorite,"Edit"))

    }

}