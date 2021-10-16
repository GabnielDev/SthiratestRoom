package com.example.shiratestroom.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shiratestroom.R
import com.example.shiratestroom.databinding.ItemListBinding
import com.example.shiratestroom.model.Note

class NoteAdapter(
    private val notes: ArrayList<Note>,
    private val listener: OnItemClickCallBack
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Note>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBinding.bind(itemView)
        fun bind(note: Note) {
            with(binding) {
                txtId.text = "Barang : " + note.id.toString()
                txtNamabarang.text = note.namabarang
                txtLokasiRak.text = note.lokasirak

                itemView.setOnClickListener {
                    listener.onItemClicked(note)
                }
                iconEdit.setOnClickListener {
                    listener.onItemUpdated(note)
                }
                iconDelete.setOnClickListener {
                    listener.onItemDeleted(note)
                }
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(note: Note)
        fun onItemUpdated(note: Note)
        fun onItemDeleted(note: Note)
    }
}