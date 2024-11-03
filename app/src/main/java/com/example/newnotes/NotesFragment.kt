package com.example.newnotes
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotesFragment : Fragment() {

    private lateinit var adapter: RecyclerAdapter
    private lateinit var notes: MutableList<Notes>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val db = DBHelper(requireContext(), null)
        notes = mutableListOf()
        val noteET = view.findViewById<EditText>(R.id.noteET)
        val updateBTN = view.findViewById<Button>(R.id.updateBTN)
        val notesListRV = view.findViewById<RecyclerView>(R.id.notesListRV)

        val onFragmentDataListener: OnFragmentDataListener = requireActivity() as OnFragmentDataListener
        notesListRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecyclerAdapter(notes, object : RecyclerAdapter.OnNotesClickListener {
            override fun onItemClick(note: Notes) {
                onFragmentDataListener.onData(note.note)
            }
        })

        notesListRV.adapter = adapter
        notesListRV.setHasFixedSize(true)
        notes.addAll((db.getInfo()))

        updateBTN.setOnClickListener {
            if (noteET.text.isEmpty()) {
                Toast.makeText(requireContext(), "Введите заметку", Toast.LENGTH_LONG).show()
            } else {
                val id = IdGenerator(db).addId()
                val note = noteET.text.toString()
                val date = SimpleDateFormat("dd:MM:yyyy HH:mm", Locale.getDefault())
                val currentDate = date.format(Date()).toString()
                val newNote = Notes(id, note, currentDate)
                noteET.text.clear()
                db.addNote(newNote)
                notes.clear()
                notes.addAll(db.getInfo())
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val db = DBHelper(requireContext(), null)
        notes.addAll(db.getInfo())
        val newNote = arguments?.getString("newNote")
        val oldNote = arguments?.getString("oldNote")
        if (newNote != null) {
            var result = 0
            for (i in notes.indices) {
                if (oldNote == notes[i].note) result = i
            }
            db.deleteNote(notes[result])
            val id = IdGenerator(db).addId()
            val date = SimpleDateFormat("dd:MM:yyyy HH:mm", Locale.getDefault())
            val currentDate = date.format(Date()).toString()
            val addNote = Notes(id, newNote, currentDate)
            db.addNote(addNote)
            notes.clear()
            notes.addAll(db.getInfo())
            adapter.notifyDataSetChanged()
        }
    }
}