package com.example.newnotes

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction

class DetailsFragment : Fragment(), OnFragmentDataListener {
    private lateinit var onFragmentDataListener: OnFragmentDataListener
    private lateinit var noteTV: TextView
    private lateinit var editBTN: Button
    private lateinit var saveBTN: Button
    private lateinit var note: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        onFragmentDataListener = requireActivity() as OnFragmentDataListener
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        noteTV = view.findViewById(R.id.noteTV)
        note = arguments?.getString("note")!!
        noteTV.text = note
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editBTN = view.findViewById(R.id.editBTN)
        saveBTN = view.findViewById(R.id.saveBTN)

        editBTN.setOnClickListener {
            editDialog()
        }

        saveBTN.setOnClickListener {
            val newNote = noteTV.text.toString()
            onData(newNote)
        }
    }

    private fun editDialog() {
        val editDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val editDialogView = inflater.inflate(R.layout.edit_dialog, null)
        editDialogBuilder.setView(editDialogView)
        val editNoteET = editDialogView.findViewById<EditText>(R.id.editNoteET)
        val cancelEditNoteBTN = editDialogView.findViewById<Button>(R.id.cancelEditNoteBTN)
        val confirmEditNoteBTN = editDialogView.findViewById<Button>(R.id.confirmEditNoteBTN)
        var note = noteTV.text.toString()
        editNoteET.setText(note)
        val dialog = editDialogBuilder.create()

        cancelEditNoteBTN.setOnClickListener {
            dialog.dismiss()
        }

        confirmEditNoteBTN.setOnClickListener {
            note = editNoteET.text.toString()
            noteTV.text = note
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onData(data: String) {
        val bundle = Bundle()
        bundle.putString("newNote", data)
        bundle.putString("oldNote", note)

        val transaction = this.fragmentManager?.beginTransaction()
        val notesFragment = NotesFragment()
        notesFragment.arguments = bundle

        transaction?.replace(R.id.fragmentContainer, notesFragment)
        transaction?.addToBackStack(null)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.commit()
    }
}