package com.example.forwaiters.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R
import com.example.forwaiters.database.ForWaitersDatabase
import com.example.forwaiters.database.TavoloPiatto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdapterListViewOrder(
    private var listaOrdini: List<TavoloPiatto>,
    private val forWaitersDatabase: ForWaitersDatabase
) : RecyclerView.Adapter<AdapterListViewOrder.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val imageButton: ImageButton = view.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_order_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaOrdini[position]
        holder.textView.text = "${item.nomeAppPiatto}: ${item.quantita}"
        holder.imageButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                forWaitersDatabase.tavoloPiattoDao.resettaQuantitaPerPiatto(item.nomeDBTavoloPiatto)
            }
        }
    }

    override fun getItemCount(): Int = listaOrdini.size

    fun updateOrdini(nuovaLista: List<TavoloPiatto>) {
        listaOrdini = nuovaLista
        notifyDataSetChanged()
    }
}


