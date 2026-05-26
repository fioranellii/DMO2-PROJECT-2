package com.hugoygor.myapplication.model

import java.util.Date

data class Nota(
    val id: String = "",
    val titulo: String = "",
    val conteudo: String = "",
    val dataCriacao: Date = Date(),
    val urlFoto: String? = null
)