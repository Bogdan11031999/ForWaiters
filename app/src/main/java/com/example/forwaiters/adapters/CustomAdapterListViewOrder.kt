package com.example.forwaiters.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R

class CustomAdapterListViewOrder(private val items: List<Pair<String, String>>) : RecyclerView.Adapter<CustomAdapterListViewOrder.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_order_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.first
        holder.button.text = item.second

        // Aggiungi eventuali listener per il bottone
        holder.button.setOnClickListener {
            // Azione per il bottone
        }
    }

    override fun getItemCount(): Int = items.size
}
