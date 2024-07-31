package com.example.forwaiters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forwaiters.adapters.AdapterGridViewMenu
import com.example.forwaiters.adapters.AdapterGridViewPlates
import com.example.forwaiters.adapters.CustomAdapterListViewOrder
import com.example.forwaiters.adapters.SpaceItemDecoration

class FloorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor)

        val recyclerViewPlates: RecyclerView = findViewById(R.id.recyclerViewPlates)
        val itemsPlates = listOf("Button 1", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6")
        val adapterPlates = AdapterGridViewPlates(itemsPlates)
        val layoutManagerPlates = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        recyclerViewPlates.layoutManager = layoutManagerPlates
        recyclerViewPlates.adapter = adapterPlates



        val recyclerViewMenu: RecyclerView = findViewById(R.id.recyclerViewMenu)
        val itemsMenu = listOf("Button 1", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2","Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6", "Button 2", "Button 3", "Button 4", "Button 5", "Button 6")
        val adapterMenu = AdapterGridViewMenu(itemsMenu)
        val layoutManagerMenu = GridLayoutManager(this,1, RecyclerView.HORIZONTAL, false)
        recyclerViewMenu.layoutManager = layoutManagerMenu
        recyclerViewMenu.adapter = adapterMenu

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewOrder)
        val itemsOrder = listOf(
            "Item 1" to "Button 1",
            "Item 2" to "Button 2",
            "Item 3" to "Button 3",
            "Item 4" to "Button 4",
            "Item 5" to "Button 5",
            "Item 6" to "Button 6"
        )
        val adapter = CustomAdapterListViewOrder(itemsOrder)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}