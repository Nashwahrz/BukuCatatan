package com.nashwa.bukucatatan.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nashwa.bukucatatan.Note
import com.nashwa.bukucatatan.NoteDatabaseHelper
import com.nashwa.bukucatatan.R
import com.nashwa.bukucatatan.UpdateNoteActivity

class NoteAdapter(
    private var notes: List<Note>,
    private val context: Context
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var db : NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val content: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            // Tampilkan dialog konfirmasi
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Konfirmasi")
                setMessage("Apakah anda ingin melanjutkan ?")
                setIcon(R.drawable.baseline_delete_24)

                setPositiveButton("Yakin") { dialogInterface, _ ->
                    // Hapus catatan dari database
                    db.deleteNote(note.id)
                    // Refresh data di adapter
                    refreshData(db.getAllNotes())

                    Toast.makeText(holder.itemView.context, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show()

                    dialogInterface.dismiss()
                }

                setNegativeButton("Batal") { dialogInterface, _ ->

                    dialogInterface.dismiss()
                }
            }.show()
        }
    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
