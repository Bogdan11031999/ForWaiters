package com.example.forwaiters.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.R
import com.example.forwaiters.database.ForWaitersDatabase
import com.example.forwaiters.database.Piatto
import com.example.forwaiters.others.InfoOrder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AdapterGridViewPlates(
    private var piattiList: List<Piatto>,
    private val forWaitersDatabase: ForWaitersDatabase
) : RecyclerView.Adapter<AdapterGridViewPlates.GridViewHolder>() {

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_plates, parent, false)
        return GridViewHolder(view)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val piatto = piattiList[position]
        holder.button.text = piatto.nomeAppPiatto
        holder.button.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                forWaitersDatabase.tavoloPiattoDao.incrementaQuantitaTavoloPiatto(
                    "${InfoOrder.tavolo!!.nomeDBTavolo}${piatto.nomeAppPiatto}"
                )
            }
        }
    }

    override fun getItemCount(): Int = piattiList.size

    fun updatePlates(newPiattiList: List<Piatto>) {
        piattiList = newPiattiList
        notifyDataSetChanged()
    }
}



