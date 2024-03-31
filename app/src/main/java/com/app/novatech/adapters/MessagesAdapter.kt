package com.app.novatech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Message

class MessagesAdapter(private val mensajes: List<Message>) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_forum, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(mensajes[position])
    }

    override fun getItemCount(): Int = mensajes.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMensaje: TextView = itemView.findViewById(R.id.tvPostContent)
        private val tvTiempo: TextView = itemView.findViewById(R.id.tvPostAuthor)
        private val tvColaborador: TextView = itemView.findViewById(R.id.tvPostTime)

        fun bind(mensaje: Message) {
            tvMensaje.text = mensaje.mensaje
            tvTiempo.text = mensaje.tiempo
            tvColaborador.text = mensaje.colaborador
        }
    }
}
