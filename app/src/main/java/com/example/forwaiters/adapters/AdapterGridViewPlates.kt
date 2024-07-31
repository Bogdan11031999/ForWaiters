package com.example.forwaiters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R

class AdapterGridViewPlates (private val items: List<String>) : RecyclerView.Adapter<AdapterGridViewPlates.GridViewHolder>() {

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_singlelem_layout, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.button.text = items[position]
        // Aggiungi qui eventuali listener per i bottoni
    }

    override fun getItemCount(): Int = items.size
}
