package com.example.forwaiters.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.others.OnMenuItemClickListener
import com.example.forwaiters.R
import com.example.forwaiters.adapters.AdapterGridViewMenu
import com.example.forwaiters.adapters.AdapterGridViewPlates
import com.example.forwaiters.adapters.AdapterListViewOrder
import com.example.forwaiters.database.Categoria
import com.example.forwaiters.database.ForWaitersDatabase
import com.example.forwaiters.others.InfoOrder
import com.example.forwaiters.others.PDFGenerator
import com.example.forwaiters.others.PDFPrinter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class TableActivity : AppCompatActivity(), OnMenuItemClickListener {

    private lateinit var listaCategoria: List<Categoria>
    private lateinit var recyclerViewPlates: RecyclerView
    private lateinit var recyclerViewOrder: RecyclerView
    private lateinit var adapterPlates: AdapterGridViewPlates
    private lateinit var adapterOrder: AdapterListViewOrder
    private lateinit var forWaitersDatabase: ForWaitersDatabase
    private lateinit var textViewTitle: TextView
    private lateinit var buttonPrint: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        initViews()
        setupRecyclerViews()
        loadCategoriesAndSetupAdapters()
        setupButtonPrintListener()
    }

    private fun initViews() {
        textViewTitle = findViewById(R.id.textViewTitle)
        buttonPrint = findViewById(R.id.buttonPrint)
        textViewTitle.text = "${InfoOrder.piano!!.nomeAppPiano}  ${InfoOrder.tavolo!!.nomeAppTavolo}"
    }

    private fun setupRecyclerViews() {
        // Inizializza RecyclerView per il menu
        findViewById<RecyclerView>(R.id.recyclerViewMenu).apply {
            layoutManager = GridLayoutManager(this@TableActivity, 2, RecyclerView.HORIZONTAL, false)
        }

        // Inizializza RecyclerView per i piatti
        recyclerViewPlates = findViewById<RecyclerView>(R.id.recyclerViewPlates).apply {
            layoutManager = GridLayoutManager(this@TableActivity, 3, RecyclerView.HORIZONTAL, false)
        }

        // Inizializza RecyclerView per gli ordini
        recyclerViewOrder = findViewById<RecyclerView>(R.id.recyclerViewOrder).apply {
            isHorizontalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(this@TableActivity)
        }

        forWaitersDatabase = ForWaitersDatabase.getInstance(applicationContext)
    }

    private fun loadCategoriesAndSetupAdapters() {
        lifecycleScope.launch(Dispatchers.IO) {
            listaCategoria = forWaitersDatabase.categoriaDao.ottieniTutteLeCategorie()
            withContext(Dispatchers.Main) {
                setupAdapters()
                observeTavoloPiattoChanges()
            }
        }
    }

    private fun setupAdapters() {
        // Adapter per il menu
        findViewById<RecyclerView>(R.id.recyclerViewMenu).adapter =
            AdapterGridViewMenu(listaCategoria, this)

        // Adapter per i piatti
        adapterPlates = AdapterGridViewPlates(InfoOrder.listaPiatti, forWaitersDatabase)
        recyclerViewPlates.adapter = adapterPlates

        // Adapter per gli ordini
        adapterOrder = AdapterListViewOrder(emptyList(), forWaitersDatabase)
        recyclerViewOrder.adapter = adapterOrder
    }

    private fun observeTavoloPiattoChanges() {
        forWaitersDatabase.tavoloPiattoDao
            .ottieniTavoloPiattoConQuantitaMaggioreDiZero(InfoOrder.tavolo!!.nomeDBTavolo)
            .observe(this) { nuovaLista ->
                adapterOrder.updateOrdini(nuovaLista)
            }
    }

    private fun setupButtonPrintListener() {
        buttonPrint.setOnClickListener {
            val outputFilePath = "${getExternalFilesDir(null)?.absolutePath}${File.separator}${InfoOrder.piano!!.nomeAppPiano}${InfoOrder.tavolo!!.nomeAppTavolo}.pdf"
            val pdfGenerator = PDFGenerator(this)

            lifecycleScope.launch(Dispatchers.IO) {
                val piattiList = forWaitersDatabase.tavoloPiattoDao.ottieniTavoloPiattoConQuantitaMaggioreDiZeroNoLive(InfoOrder.tavolo!!.nomeDBTavolo)
                val categoriaList = forWaitersDatabase.categoriaDao.ottieniTutteLeCategorie()

                withContext(Dispatchers.Main) {
                    pdfGenerator.generateAndPrintPDF(piattiList, categoriaList, outputFilePath) { pdfFile ->
                        PDFPrinter.printPDF(this@TableActivity, pdfFile)
                    }
                }
            }
        }
    }

    override fun onMenuItemClick(categoria: Categoria) {
        lifecycleScope.launch(Dispatchers.IO) {
            val piatti = forWaitersDatabase.piattoDao.trovaPiattiPerCategoria(categoria.nomeAppCategoria)
            withContext(Dispatchers.Main) {
                adapterPlates.updatePlates(piatti)
            }
        }
    }
}
