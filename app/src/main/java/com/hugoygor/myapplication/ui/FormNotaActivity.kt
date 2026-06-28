package com.hugoygor.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoygor.myapplication.databinding.ActivityFormNotaBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import kotlin.math.sqrt

class FormNotaActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityFormNotaBinding
    private val db = FirebaseFirestore.getInstance()

    private var imageBitmap: Bitmap? = null
    private var audioBase64: String = ""

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var isRecording = false

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lightSensor: Sensor? = null

    private var accelerationCurrent = 0f
    private var accelerationLast = 0f
    private var shake = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        accelerationCurrent = SensorManager.GRAVITY_EARTH
        accelerationLast = SensorManager.GRAVITY_EARTH
        shake = 0.00f

        // NOVO: Faz o botão de voltar fechar a tela atual e retornar para a anterior
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnCamera.setOnClickListener {
            abrirCamera()
        }

        binding.btnAudio.setOnClickListener {
            gerenciarGravação()
        }

        binding.btnSalvar.setOnClickListener {
            salvarNota()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                imageBitmap = bitmap
                binding.imgPreview.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Câmera cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    private fun abrirCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            return
        }
        cameraLauncher.launch(null)
    }

    private fun gerenciarGravação() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            return
        }

        if (!isRecording) {
            audioFile = File(cacheDir, "audio_nota.3gp")
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
            isRecording = true
            binding.btnAudio.text = "Parar Gravação"
            Toast.makeText(this, "Gravando...", Toast.LENGTH_SHORT).show()
        } else {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            binding.btnAudio.text = "Gravar Áudio"
            Toast.makeText(this, "Áudio gravado!", Toast.LENGTH_SHORT).show()

            audioFile?.let {
                audioBase64 = converterAudioParaBase64(it)
            }
        }
    }

    private fun converterAudioParaBase64(file: File): String {
        val bytes = FileInputStream(file).use { it.readBytes() }
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun salvarNota() {
        val titulo = binding.edtTitulo.text.toString().trim()
        val descricao = binding.edtDescricao.text.toString().trim()

        if (titulo.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val bitmap = imageBitmap
        var imageBase64 = ""

        if (bitmap != null) {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
            val byteArray = baos.toByteArray()
            imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        salvarFirestore(titulo, descricao, imageBase64, audioBase64)
    }

    private fun salvarFirestore(titulo: String, descricao: String, imageBase64: String, audioBase64: String) {
        val nota = hashMapOf(
            "titulo" to titulo,
            "descricao" to descricao,
            "imageUrl" to imageBase64,
            "audioUrl" to audioBase64,
            "data" to System.currentTimeMillis()
        )

        db.collection("notas")
            .add(nota)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota salva!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro no Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                accelerationLast = accelerationCurrent
                accelerationCurrent = sqrt(x * x + y * y + z * z)
                val delta = accelerationCurrent - accelerationLast
                shake = shake * 0.9f + delta

                if (shake > 12) {
                    binding.edtTitulo.text?.clear()
                    binding.edtDescricao.text?.clear()
                    Toast.makeText(this, "Campos limpos!", Toast.LENGTH_SHORT).show()
                }
            } else if (event.sensor.type == Sensor.TYPE_LIGHT) {
                val lux = event.values[0]
                if (lux < 20) {
                    binding.layoutForm.setBackgroundColor(Color.parseColor("#212121"))
                } else {
                    binding.layoutForm.setBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}