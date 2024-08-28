package com.example.forwaiters.others

import com.example.forwaiters.database.Categoria
import com.example.forwaiters.database.Piano
import com.example.forwaiters.database.Piatto
import com.example.forwaiters.database.Tavolo


object InfoOrder {
    var piano: Piano? = null
    var tavolo: Tavolo? = null
    var categoria: Categoria? = Categoria("",0)
    var piatto: Piatto? = null
    var listaPiatti = listOf<Piatto>()
    var listaCategorie = listOf<Categoria>()
}