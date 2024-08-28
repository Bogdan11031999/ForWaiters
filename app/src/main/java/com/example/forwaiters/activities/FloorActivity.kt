package com.example.forwaiters.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R
import com.example.forwaiters.adapters.AdapterGridViewTables
import com.example.forwaiters.database.ForWaitersDatabase
import com.example.forwaiters.database.Piatto
import com.example.forwaiters.database.Tavolo
import com.example.forwaiters.others.InfoOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FloorActivity : AppCompatActivity() {

    private lateinit var recyclerViewPlates: RecyclerView
    private lateinit var textViewTitle: TextView
    private lateinit var layoutManagerPlates: GridLayoutManager
    private lateinit var forWaitersDatabase: ForWaitersDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor)

        initViews()
        setupRecyclerView()
        loadData()
    }

    private fun initViews() {
        recyclerViewPlates = findViewById(R.id.recyclerViewTables)
        textViewTitle = findViewById(R.id.title)
        layoutManagerPlates = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
    }

    private fun setupRecyclerView() {
        recyclerViewPlates.layoutManager = layoutManagerPlates
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            forWaitersDatabase = ForWaitersDatabase.getInstance(applicationContext)

            val listaTavoli = loadTavoli()
            InfoOrder.listaPiatti = loadPiatti()

            updateUI(listaTavoli)
        }
    }

    private fun loadTavoli(): List<Tavolo> {
        return forWaitersDatabase.tavoloDao.trovaTavoliPerPiano(InfoOrder.piano!!.nomeDBPiano)
    }

    private fun loadPiatti(): List<Piatto> {
        return forWaitersDatabase.piattoDao.trovaPiattiPerCategoria(InfoOrder.listaCategorie[0].nomeAppCategoria)
    }

    private suspend fun updateUI(listaTavoli: List<Tavolo>) {
        withContext(Dispatchers.Main) {
            recyclerViewPlates.adapter = AdapterGridViewTables(this@FloorActivity, listaTavoli)
            textViewTitle.text = InfoOrder.piano!!.nomeAppPiano
        }
    }
}
