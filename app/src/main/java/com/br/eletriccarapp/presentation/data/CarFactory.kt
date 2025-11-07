package com.br.eletriccarapp.presentation.data

import com.br.eletriccarapp.presentation.domain.Carro

object CarFactory {

    val list = listOf (
        Carro (
            id = 1,
            preco = "R$ 300.000,00",
            bateria = "300 kWh",
            potencia = "200cv",
            recarga = "30 min",
            urlPhoto = "www.google.com.br"

        ) ,
        Carro (
            id = 2,
            preco = "R$ 300.000,00",
            bateria = "300 kWh",
            potencia = "200cv",
            recarga = "30 min",
            urlPhoto = "www.google.com.br"

        )
    )
}