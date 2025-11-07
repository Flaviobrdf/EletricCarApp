package com.br.eletriccarapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.br.eletriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var preco: EditText
    lateinit var btnCalcular : Button
    lateinit var kmPercorrido: EditText
    lateinit var resultado: TextView
    lateinit var btnVoltar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView()
        setupListeners()

    }
    fun setupView(){
        preco = findViewById(R.id.et_preco_km)
        kmPercorrido = findViewById(R.id.et_km_percorrido)
        btnCalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)
        btnVoltar = findViewById(R.id.iv_voltar)
    }

    fun setupListeners(){
        btnCalcular.setOnClickListener{
            //val textoDitado = preco.text.toString()
            //Log.d("Texto digitado", textoDitado)
            calcular()

        }
        btnVoltar.setOnClickListener( {
            finish()
        })
    }

    fun calcular () {
        val preco = preco.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()

        val result = preco * km

        resultado.text = "R$: " + result.toString()
        Log.d("Resultado -> ", result.toString())
    }

}