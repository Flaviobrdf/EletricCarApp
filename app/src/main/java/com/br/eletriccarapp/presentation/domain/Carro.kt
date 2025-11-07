package com.br.eletriccarapp.presentation.domain

data class Carro (
    val id: Int,
    val preco: String,
    val bateria: String,
    val potencia: String,
    val recarga: String,
    val urlPhoto: String
)