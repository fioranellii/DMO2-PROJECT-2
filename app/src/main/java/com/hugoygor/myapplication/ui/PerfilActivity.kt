package com.hugoygor.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoygor.myapplication.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDados()

        binding.btnSair.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarDados() {

        val user = auth.currentUser

        if (user == null) {
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        db.collection("usuarios")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {
                    binding.txtNome.text = doc.getString("nome") ?: "Sem nome"
                    binding.txtEmail.text = doc.getString("email") ?: "Sem email"
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show()
            }
    }
}