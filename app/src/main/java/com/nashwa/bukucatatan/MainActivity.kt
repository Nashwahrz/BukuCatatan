package com.nashwa.bukucatatan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nashwa.bukucatatan.adapter.NoteAdapter
import com.nashwa.bukucatatan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        noteAdapter = NoteAdapter(emptyList(), this)

        // Setup RecyclerView
        binding.noteRecyvleview.layoutManager = LinearLayoutManager(this)
        binding.noteRecyvleview.adapter = noteAdapter

        // Button to add new note
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // Load notes from database
        loadNotes()
    }

    // Function to load all notes from the database
    private fun loadNotes() {
        val notes = db.getAllNotes()
        Log.d("MainActivity", "Loaded ${notes.size} notes.")
        noteAdapter.refreshData(notes)
    }

    // Reload notes when returning to MainActivity
    override fun onResume() {
        super.onResume()
        loadNotes()  // Load updated notes after adding a new one
    }
}
