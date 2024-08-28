package com.example.forwaiters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.others.OnMenuItemClickListener
import com.example.forwaiters.R
import com.example.forwaiters.database.Categoria

class AdapterGridViewMenu(
    private val listCategoria: List<Categoria>,
    private val listener: OnMenuItemClickListener
) : RecyclerView.Adapter<AdapterGridViewMenu.ButtonViewHolder>() {

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_menu, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val categoria = listCategoria[position]
        holder.button.text = categoria.nomeAppCategoria
        holder.button.setOnClickListener {
            listener.onMenuItemClick(categoria)
        }
    }

    override fun getItemCount(): Int = listCategoria.size
}


