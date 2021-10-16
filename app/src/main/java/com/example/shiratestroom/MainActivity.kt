package com.example.shiratestroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shiratestroom.adapter.NoteAdapter
import com.example.shiratestroom.databinding.ActivityMainBinding
import com.example.shiratestroom.model.Note
import com.example.shiratestroom.room.NoteDB
import com.example.shiratestroom.utils.Constants.TYPE_CREATE
import com.example.shiratestroom.utils.Constants.TYPE_READ
import com.example.shiratestroom.utils.Constants.TYPE_UPDATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val db by lazy { NoteDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            Log.d("TAG", "onStart: $notes")
            withContext(Dispatchers.Main) {
                noteAdapter.setData(notes)
            }
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnItemClickCallBack {
            override fun onItemClicked(note: Note) {
                intentEdit(note.id, TYPE_READ)
            }

            override fun onItemUpdated(note: Note) {
                intentEdit(note.id, TYPE_UPDATE)
            }

            override fun onItemDeleted(note: Note) {
                deleteDialog(note)
            }
        })

        binding.listNote.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    private fun intentEdit(noteId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupListener() {
        binding.buttonCreate.setOnClickListener {
            intentEdit(0, TYPE_CREATE)
        }
    }

    private fun deleteDialog(note: Note) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are you sure to delete ${note.namabarang}")
            setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    loadData()
                }
            }
        }

        alertDialog.show()
        alertDialog.setCancelable(false)
    }

}