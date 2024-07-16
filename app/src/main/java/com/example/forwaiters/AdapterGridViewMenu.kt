package com.example.forwaiters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AdapterGridViewMenu(private val items: List<String>) : RecyclerView.Adapter<AdapterGridViewMenu.ButtonViewHolder>() {

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_menu_layout, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.button.text = items[position]
        // Aggiungi eventuali listener per i bottoni
        holder.button.setOnClickListener {
            // Azione per il bottone
        }
    }

    override fun getItemCount(): Int = items.size
}
