package com.hugoygor.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoygor.myapplication.auth.UserAuth
import com.hugoygor.myapplication.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val userAuth = UserAuth()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarCliques()
    }

    private fun configurarCliques() {
        binding.btnCriarConta.setOnClickListener {
            realizarCadastro()
        }
        binding.btnJaTenhoConta.setOnClickListener {
            finish()
        }
    }

    private fun realizarCadastro() {
        val nome = binding.edtNome.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val senha = binding.edtPassword.text.toString().trim()
        val confirmarSenha = binding.edtConfirmPassword.text.toString().trim()

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            exibirMensagem("Preencha todos os campos")
            return
        }

        if (senha != confirmarSenha) {
            exibirMensagem("As senhas não coincidem")
            return
        }

        if (senha.length < 6) {
            exibirMensagem("A senha deve ter pelo menos 6 caracteres")
            return
        }

        userAuth.cadastro(email, senha) { sucesso, mensagemErro ->
            if (sucesso) {
                salvarDadosFirestore(email, nome)
            } else {
                exibirMensagem(mensagemErro ?: "Erro ao criar conta")
            }
        }
    }

    private fun salvarDadosFirestore(email: String, nome: String) {
        val dados = hashMapOf(
            "email" to email,
            "nomeCompleto" to nome,
            "username" to nome,
            "fotoPerfil" to "",
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("usuarios")
            .document(email)
            .set(dados)
            .addOnSuccessListener {
                exibirMensagem("Cadastro realizado com sucesso!")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                exibirMensagem("Erro ao salvar dados: ${e.message}")
            }
    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
    }
}