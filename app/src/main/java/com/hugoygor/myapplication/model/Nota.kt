package com.hugoygor.myapplication.model

data class Nota(
    val titulo: String = "",
    val descricao: String = "",
    val imageUrl: String = "",
    val audioBase64: String = "",
    val data: Long = 0
)