package com.hugoygor.myapplication.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hugoygor.myapplication.R
import java.util.Locale

class FormNotaActivity : AppCompatActivity() {

    private lateinit var etConteudo: EditText
    private lateinit var btnMicrofone: Button
    private val REQUEST_CODE_SPEECH = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_nota)

        etConteudo = findViewById(R.id.etConteudo)
        btnMicrofone = findViewById(R.id.btnMicrofone)

        btnMicrofone.setOnClickListener {
            ouvirUsuario()
        }
    }

    private fun ouvirUsuario() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale o conteúdo da sua nota...")
        }

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH)
        } catch (e: Exception) {
            Toast.makeText(this, "Seu dispositivo não suporta comando de voz.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textoDitado = result?.get(0) ?: ""

            val textoAtual = etConteudo.text.toString()
            etConteudo.setText("$textoAtual $textoDitado".trim())
        }
    }
}