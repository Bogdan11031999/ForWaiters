package com.example.forwaiters

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonFloorOne = findViewById<Button>(R.id.buttonFloorOne)
        val buttonFloorZero = findViewById<Button>(R.id.buttonFloorZero)
        buttonFloorOne.setOnClickListener {
            val intent = Intent(this,FloorActivity::class.java)
            intent.putExtra("one",1)
            startActivity(intent)
        }
        buttonFloorZero.setOnClickListener {
            val intent = Intent(this,FloorActivity::class.java)
            intent.putExtra("zero",0)
            startActivity(intent)
        }
    }
}