package com.hugoygor.myapplication.model

object NotaDao {

    private val notas = mutableListOf<Nota>()

    fun adiciona(nota: Nota) {
        notas.add(nota)
    }

    fun buscaTodas(): List<Nota> {
        return notas.toList()
    }
}