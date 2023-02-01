package com.task.noteapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.task.noteapp.databinding.FragmentEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*


class EditNoteFragment : Fragment(){

    //declaring variables as late init so we can use them in functions too
    private lateinit var binding : FragmentEditNoteBinding
    private lateinit var viewmodel: NoteViewModel
    private lateinit var imgUrl : String
    //cant create late init boolean
    private lateinit var isFav : String
    private lateinit var fav : String

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //inflating the fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_edit_note, container, false)

        //initializing viewmodel
        viewmodel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        //getting data from NotesListFragment using SafeArgs
        val args = EditNoteFragmentArgs.fromBundle(requireArguments())
        val noteTitle = args.noteTitle
        val noteDesc = args.noteDescription
        val noteID = args.noteId
        val noteImage: String? = args.noteImage
        val noteType: String = args.noteType
        isFav= args.noteFav.toString()

        //initializing variables
        fav="false"
        imgUrl= noteImage.toString()

        // on below line we are setting data to edit texts
        binding.idEdtNoteTitle.setText(noteTitle)
        binding.idEdtNoteDesc.setText(noteDesc)

        //showing image of previously created note in edit fragment if not null
        noteImage?.let { viewmodel.convertImage(it,binding.noteImg) }

        //setting icon of favorite button according to how it was in list fragment
        if (isFav.toBoolean()) {
            binding.imgFav.setImageResource(R.drawable.ic_favorite)
        }
        else binding.imgFav.setImageResource(R.drawable.ic_favorite_border)

        //save note button
        binding.idFAB.setOnClickListener {

            val isFavBool = fav.toBoolean()

            //if note is being edited the following code will run to update the note
            if (validNoteInput(binding.idEdtNoteTitle.text.toString(), binding.idEdtNoteDesc.text.toString())) {

                if(noteType =="Edit") {

                    //getting editing date
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val editingDate: String = sdf.format(Date())

                    //updating NoteEntity
                    val updatedNote = NoteEntity(
                        binding.idEdtNoteTitle.text.toString(),
                        binding.idEdtNoteDesc.text.toString(),
                        imgUrl,
                        editingDate,
                        "Edited",
                        isFavBool
                    )
                    updatedNote.id = noteID
                    viewmodel.updateNote(updatedNote)

                    //navigating back to List fragment
                    findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToNotesListFragment())
                }

                //if note is being created for first time the following code will run to save the note
                else{

                    //getting creating date
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val currentDateAndTime: String = sdf.format(Date())

                    // if the string is not empty we are adding new NoteEntity
                    viewmodel.addNote(
                        NoteEntity(
                            binding.idEdtNoteTitle.text.toString(),
                            binding.idEdtNoteDesc.text.toString(),
                            imgUrl,
                            currentDateAndTime,
                            "Created",
                            isFavBool
                        )
                    )
                    Toast.makeText(activity, "${binding.idEdtNoteTitle.text} Added", Toast.LENGTH_SHORT).show()

                    //navigating back to List fragment
                    findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToNotesListFragment())
                }
            }

            else{
                Toast.makeText(activity,"Please fill note Title and Description.",Toast.LENGTH_SHORT).show()
            }

        }

        //navigating back to list fragment when back button is pressed
        binding.imgBack.setOnClickListener{

            findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToNotesListFragment())

        }

        //setting icon of favorite button when it's clicked
        binding.imgFav.setOnClickListener {

            if (isFav == "true"){
                fav="false"
                binding.imgFav.setImageResource(R.drawable.ic_favorite_border)
                //  item.isVisible = true

            }
            else {
                fav="true"
                binding.imgFav.setImageResource(R.drawable.ic_favorite)
            }
        }

        //adding a dialog to image button and setting it to imageview
        binding.idImgFAB.setOnClickListener{
            val inputEditTextField = EditText(requireActivity())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Image URL")
                .setMessage("Please enter image URL")
                .setView(inputEditTextField)
                .setPositiveButton("OK") { _, _ ->
                    imgUrl = inputEditTextField.text.toString()
                    if(validUrl(imgUrl)) viewmodel.convertImage(imgUrl,binding.noteImg)
                   else Toast.makeText(activity,"Please enter valid URL.",Toast.LENGTH_SHORT).show()

                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()

        }

        return binding.root
    }

    //navigating back to list fragment once user uses android automatic going back option
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToNotesListFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    fun validNoteInput(
        title : String,
        desc : String,
    ) : Boolean {
        // title or desc are empty return false
        if (title.isEmpty() || desc.isEmpty()){
            return false
        }
        return true
    }

    fun validUrl(
        url : String,
    ) : Boolean {
        // title or desc are empty return false
        if ( URLUtil.isValidUrl(url)){
            return true
        }
        return false
    }

}

