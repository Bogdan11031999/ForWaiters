package com.example.forwaiters.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R
import com.example.forwaiters.activities.TableActivity
import com.example.forwaiters.database.Tavolo
import com.example.forwaiters.others.InfoOrder


class AdapterGridViewTables(
    private val context: Context,
    private val items: List<Tavolo>
) : RecyclerView.Adapter<AdapterGridViewTables.ButtonViewHolder>() {

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_tables, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item.nomeAppTavolo
        holder.button.setOnClickListener {
            InfoOrder.tavolo = item
            val intent = Intent(context, TableActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
