package com.hugoygor.myapplication.ui

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hugoygor.myapplication.R
import java.io.File
import java.io.FileOutputStream

class DetalheNotaActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_nota)

        val titulo = intent.getStringExtra("titulo")
        val descricao = intent.getStringExtra("descricao")
        val imageUrl = intent.getStringExtra("imageUrl")
        val audioBase64 = intent.getStringExtra("audioBase64")

        findViewById<TextView>(R.id.txtTitulo).text = titulo
        findViewById<TextView>(R.id.txtDescricao).text = descricao

        val imgNota = findViewById<ImageView>(R.id.imgNota)
        val btnOuvir = findViewById<Button>(R.id.btnOuvir)

        if (!imageUrl.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(imageUrl, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                if (bitmap != null) {
                    imgNota.visibility = View.VISIBLE
                    imgNota.setImageBitmap(bitmap)
                } else {
                    imgNota.visibility = View.GONE
                }
            } catch (e: Exception) {
                imgNota.visibility = View.GONE
            }
        } else {
            imgNota.visibility = View.GONE
        }

        if (!audioBase64.isNullOrEmpty()) {
            btnOuvir.visibility = View.VISIBLE
            btnOuvir.setOnClickListener {
                tocarAudio(audioBase64)
            }
        } else {
            btnOuvir.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }
    }

    private fun tocarAudio(base64String: String) {
        try {
            val audioBytes = Base64.decode(base64String, Base64.DEFAULT)
            val tempFile = File.createTempFile("playing_audio", "3gp", cacheDir)
            val fos = FileOutputStream(tempFile)
            fos.write(audioBytes)
            fos.close()

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}