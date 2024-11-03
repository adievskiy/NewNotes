package com.example.newnotes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    private val notes: MutableList<Notes>,
    private val onItemClickListener: OnNotesClickListener,
) :
    RecyclerView.Adapter<RecyclerAdapter.NotesViewHolder>() {

    interface OnNotesClickListener {
        fun onItemClick(note: Notes)
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idTV: TextView = itemView.findViewById(R.id.idTV)
        var dateTV: TextView = itemView.findViewById(R.id.dateTV)
        var noteTV: TextView = itemView.findViewById(R.id.noteTV)

        init {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(notes[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.idTV.text = "№ ${note.id}"
        holder.dateTV.text = note.date
        holder.noteTV.text = note.note
    }
}
