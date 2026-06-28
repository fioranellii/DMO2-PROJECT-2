package com.hugoygor.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoygor.myapplication.adapter.ListaNotasAdapter
import com.hugoygor.myapplication.databinding.ActivityMainBinding
import com.hugoygor.myapplication.model.Nota

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNotas.layoutManager = LinearLayoutManager(this)

        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.btnSair.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.fabAdicionarNota.setOnClickListener {
            startActivity(Intent(this, FormNotaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        carregarNotas()
    }

    private fun carregarNotas() {
        db.collection("notas")
            .get()
            .addOnSuccessListener { result ->
                val lista = mutableListOf<Nota>()
                for (doc in result) {
                    lista.add(
                        Nota(
                            titulo = doc.getString("titulo") ?: "",
                            descricao = doc.getString("descricao") ?: "",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            audioBase64 = doc.getString("audioUrl") ?: "",
                            data = doc.getLong("data") ?: 0
                        )
                    )
                }
                binding.rvNotas.adapter = ListaNotasAdapter(lista)
            }
    }
}