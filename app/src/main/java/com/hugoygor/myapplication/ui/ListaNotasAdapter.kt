package com.hugoygor.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugoygor.myapplication.R
import com.hugoygor.myapplication.model.Nota

class ListaNotasAdapter(
    private val notas: List<Nota>
) : RecyclerView.Adapter<ListaNotasAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val conteudo: TextView = itemView.findViewById(R.id.tvConteudo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val nota = notas[position]

        holder.titulo.text = nota.titulo
        holder.conteudo.text = nota.conteudo
    }

    override fun getItemCount(): Int = notas.size
}