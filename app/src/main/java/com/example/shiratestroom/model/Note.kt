package com.example.shiratestroom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namabarang: String,
    val lokasirak: String
)
