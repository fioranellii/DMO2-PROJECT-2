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
import com.hugoygor.myapplication.model.Nota
import com.hugoygor.myapplication.model.NotaDao
import java.util.Locale
import java.util.UUID

class FormNotaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etConteudo: EditText
    private lateinit var btnMicrofone: Button
    private lateinit var btnSalvar: Button

    private val REQUEST_CODE_SPEECH = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_nota)

        etTitulo = findViewById(R.id.etTitulo)
        etConteudo = findViewById(R.id.etConteudo)
        btnMicrofone = findViewById(R.id.btnMicrofone)
        btnSalvar = findViewById(R.id.btnSalvar)

        btnMicrofone.setOnClickListener {
            ouvirUsuario()
        }

        btnSalvar.setOnClickListener {

            val nota = Nota(
                id = UUID.randomUUID().toString(),
                titulo = etTitulo.text.toString(),
                conteudo = etConteudo.text.toString()
            )

            NotaDao.adiciona(nota)

            finish()
        }
    }

    private fun ouvirUsuario() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Reconhecimento de voz indisponível",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(requestCode, resultCode, data)

        if (
            requestCode == REQUEST_CODE_SPEECH &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {

            val resultado =
                data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )

            etConteudo.append(" ${resultado?.get(0)}")
        }
    }
}