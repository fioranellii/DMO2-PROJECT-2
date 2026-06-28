package com.hugoygor.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugoygor.myapplication.R
import com.hugoygor.myapplication.model.Nota
import com.hugoygor.myapplication.ui.DetalheNotaActivity

class ListaNotasAdapter(
    private val lista: List<Nota>
) : RecyclerView.Adapter<ListaNotasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descricao: TextView = view.findViewById(R.id.tvDescricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.titulo.text = item.titulo
        holder.descricao.text = item.descricao

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalheNotaActivity::class.java)
            intent.putExtra("titulo", item.titulo)
            intent.putExtra("descricao", item.descricao)
            intent.putExtra("imageUrl", item.imageUrl)
            intent.putExtra("audioBase64", item.audioBase64)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = lista.size
}