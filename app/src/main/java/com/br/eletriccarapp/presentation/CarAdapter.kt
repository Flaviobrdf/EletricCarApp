package com.br.eletriccarapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.eletriccarapp.R
import com.br.eletriccarapp.presentation.domain.Carro

class CarAdapter (private val carros: List<Carro>): RecyclerView.Adapter <RecyclerView.ViewHolder> () {

    var carItemLister : (Carro) ->Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        holder.preco.text = carros [position].preco
        holder.bateria.text = carros [position].bateria
        holder.potencia.text = carros [position].potencia
        holder.recarga.text = carros [position].recarga
        holder.favorito.setOnClickListener{
            val carro = carros[position]
            carItemLister(carro)
            setupFavorite(carro,holder)


        }


    }

    private fun setupFavorite(
        carro: Carro,
        holder: ViewHolder
    ) {
        carro.isFavorite = !carro.isFavorite

        if(carro.isFavorite){
            holder.favorito.setImageResource(R.drawable.star_selected)
        } else {
            holder.favorito.setImageResource(R.drawable.star)
        }
    }

    override fun getItemCount(): Int {
        return carros.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView
        val favorito: ImageView


        init {
            view.apply {
                preco = view.findViewById(R.id.tv_preco_value)
                bateria = view.findViewById(R.id.tv_bateria_value)
                potencia = view.findViewById(R.id.tv_potencia_value)
                recarga = view.findViewById(R.id.tv_recarga_value)
                favorito = view.findViewById(R.id.favorite)
            }

        }
    }

}