package com.example.shiratestroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shiratestroom.databinding.ActivityEditBinding
import com.example.shiratestroom.model.Note
import com.example.shiratestroom.room.NoteDB
import com.example.shiratestroom.utils.Constants.TYPE_CREATE
import com.example.shiratestroom.utils.Constants.TYPE_READ
import com.example.shiratestroom.utils.Constants.TYPE_UPDATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var noteId: Int = 0

    private val db by lazy { NoteDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListener()
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (intent.getIntExtra("intent_type", 0)) {
            TYPE_CREATE -> {
                binding.buttonUpdate.visibility = View.GONE
            }
            TYPE_READ -> {
                binding.buttonSave.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                getNote()
            }
            TYPE_UPDATE -> {
                binding.buttonSave.visibility = View.GONE
                getNote()
            }
        }
    }

    private fun getNote() {
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote(noteId)[0]
            binding.edtNamabarang.setText(notes.namabarang)
            binding.edtLokasirak.setText(notes.lokasirak)
        }
    }

    private fun setupListener() {
        binding.buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    Note(
                        0,
                        binding.edtNamabarang.text.toString(),
                        binding.edtLokasirak.text.toString()
                    )
                )
                finish()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    Note(
                        noteId,
                        binding.edtNamabarang.text.toString(),
                        binding.edtLokasirak.text.toString()
                    )
                )
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}