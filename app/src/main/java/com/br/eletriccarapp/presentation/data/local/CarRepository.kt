package com.br.eletriccarapp.presentation.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.TABLE_NAME
import com.br.eletriccarapp.presentation.domain.Carro
import com.br.eletriccarapp.presentation.data.local.CarrosContract
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_CAR_ID


class CarRepository (private val context: Context){

    fun save(carro: Carro): Boolean {
        var isSaved = false
        try {
            val dbHelper = CarsDbhelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME_CAR_ID, carro.id)
                put(COLUMN_NAME_PRECO, carro.preco)
                put(COLUMN_NAME_BATERIA, carro.bateria)
                put(COLUMN_NAME_POTENCIA, carro.potencia)
                put(COLUMN_NAME_RECARGA, carro.recarga)
                put(COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
            }
            val inserted = db?.insert(TABLE_NAME, null, values)

            if (inserted != null) {
                isSaved = true
            }

        } catch (ex: Exception) {
            ex.message?.let {
                Log.e("Erro ao inserir -> ", it)
            }
        }

        return isSaved
    }

    fun findCarById(id: Int) : Carro? {
        val dbHelper = CarsDbhelper(context)
        val db = dbHelper.readableDatabase

        val colums = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )
        val filter = "${COLUMN_NAME_CAR_ID} = ?"
        val flterValues = arrayOf(id.toString())
        val cursor = db.query(
            TABLE_NAME,
            colums,
            filter,
            flterValues,
            null,
            null,
            null
        )


        var itemId: Long = 0
        var preco = ""
        var bateria = ""
        var potencia = ""
        var recarga = ""
        var urlPhoto = ""

        with(cursor) {

            while (moveToNext()) {
                itemId = getLong(getColumnIndexOrThrow((COLUMN_NAME_CAR_ID)))
                Log.d("ID ->", itemId.toString())

                preco = getLong(getColumnIndexOrThrow((COLUMN_NAME_PRECO))).toString()
                Log.d("preco ->", preco.toString())

                bateria = getLong(getColumnIndexOrThrow((COLUMN_NAME_BATERIA))).toString()
                Log.d("bateria ->", bateria.toString())

                potencia = getLong(getColumnIndexOrThrow((COLUMN_NAME_POTENCIA))).toString()
                Log.d("potencia ->", potencia.toString())

                recarga = getLong(getColumnIndexOrThrow((COLUMN_NAME_RECARGA))).toString()
                Log.d("recarga ->", recarga.toString())

                urlPhoto = getLong(getColumnIndexOrThrow((COLUMN_NAME_URL_PHOTO))).toString()
                Log.d("urlPhoto ->", urlPhoto.toString())


            }
            cursor.close()
            return Carro (
                id = itemId.toInt(),
                preco = preco,
                bateria = bateria,
                potencia = potencia,
                recarga = recarga,
                urlPhoto = urlPhoto,
                isFavorite = true
            )
        }

    }
    fun saveIfNotExist(carro: Carro) {
        val car = findCarById(carro.id)
        if (car != null) {
            if(car.id == ID_WHEN_NO_CAR) {
                save(carro)
            }
        }
    }

//    fun getAllCar () : List<Carro> {
//
//    }


    companion object {
        const val ID_WHEN_NO_CAR = 0
    }
}