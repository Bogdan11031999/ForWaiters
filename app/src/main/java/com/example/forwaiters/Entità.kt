package com.example.forwaiters

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Piano(
    @PrimaryKey val nomeDBPiano: String,
    val nomeAppPiano: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Piano::class,
        parentColumns = arrayOf("nomeDBPiano"),
        childColumns = arrayOf("pianoNomeDB"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["pianoNomeDB"])]
)
data class Tavolo(
    @PrimaryKey val nomeDBTavolo: String,
    val nomeAppTavolo: String,
    val pianoNomeDB: String,
    var numeroPersone: Int
)

@Entity
data class Categoria(
    @PrimaryKey 
    val nomeAppCategoria: String,
    val ordine: Int
)

@Entity(indices = [Index(value = ["nomeAppCategoria"])])
data class Piatto(
    @PrimaryKey val nomeAppPiatto: String,
    val nomeAppCategoria: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Tavolo::class,
            parentColumns = arrayOf("nomeDBTavolo"),
            childColumns = arrayOf("nomeDBTavolo"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Piatto::class,
            parentColumns = arrayOf("nomeAppPiatto"),
            childColumns = arrayOf("nomeAppPiatto"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["nomeDBTavolo"]), Index(value = ["nomeAppPiatto"])]
)
data class TavoloPiatto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeDBTavoloPiatto: String,
    val nomeDBTavolo: String,
    val nomeAppPiatto: String,
    val nomeAppCategoria: String,
    var quantita: Double = 0.0
)




