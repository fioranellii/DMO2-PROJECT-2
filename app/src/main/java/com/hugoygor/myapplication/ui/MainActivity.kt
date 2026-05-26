package com.hugoygor.myapplication.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hugoygor.myapplication.R

class MainActivity : AppCompatActivity() {

    private lateinit var fabAdicionarNota: FloatingActionButton
    private lateinit var rvNotas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAdicionarNota = findViewById(R.id.fabAdicionarNota)
        rvNotas = findViewById(R.id.rvNotas)

        rvNotas.layoutManager = LinearLayoutManager(this)

        fabAdicionarNota.setOnClickListener {
            val intent = Intent(this, FormNotaActivity::class.java)
            startActivity(intent)
        }
    }
}