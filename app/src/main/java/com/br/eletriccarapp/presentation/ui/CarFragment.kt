package com.br.eletriccarapp.presentation.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.br.eletriccarapp.R
import com.br.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.br.eletriccarapp.presentation.CarAdapter
import com.br.eletriccarapp.presentation.data.CarFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log
import android.widget.ProgressBar
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import com.br.eletriccarapp.presentation.domain.Carro
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.br.eletriccarapp.presentation.data.local.CarRepository
import com.br.eletriccarapp.presentation.data.local.CarrosContract
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.br.eletriccarapp.presentation.data.local.CarrosContract.CarEntry.TABLE_NAME
import com.br.eletriccarapp.presentation.data.local.CarsDbhelper

class CarFragment: Fragment() {
    lateinit var fabCalcular : FloatingActionButton
    lateinit var listaCarros: RecyclerView
    lateinit var progress: ProgressBar

    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView

    var carrosArray : ArrayList<Carro> = ArrayList()

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        //setupList()
        setupListeners()

        //checkForInternet(context)

    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            callService()
        } else {
            emptyState()
        }
    }

    fun emptyState() {
        progress.isVisible = false
        listaCarros.isVisible = false
        noInternetText.isVisible = true
        noInternetImage.isVisible = true
    }

    fun setupView(view: View){
        view.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
            progress = findViewById(R.id.pb_loader)
            noInternetImage = findViewById(R.id.iv_empty)
            noInternetText = findViewById(R.id.no_wifi)
        }

    }

    fun setupList(){

        val carroAdapter = CarAdapter(carrosArray)
        listaCarros.apply {
            isVisible = true
            adapter = carroAdapter
        }
        carroAdapter.carItemLister = { carro ->
            //val bateria =  carro.bateria

            val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }
    }

    fun setupListeners() {
        fabCalcular.setOnClickListener {

            //val textoDitado = preco.text.toString()
            //Log.d("Texto digitado", textoDitado)
            //calcular()
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))

        }

    }


    fun callService(){
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(urlBase)
        progress.isVisible = true
    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null
            var response = "" // ✅ Declarado fora do try

            try {
                val urlBase = URL(url[0])
                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 600000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "Appication/json"
                )
                val responseCode = urlConnection.responseCode



                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = urlConnection.inputStream.bufferedReader().use { it.readText() }

                    publishProgress(response)
                } else {
                    Log.e("Erro", "Serviço Indisponível")
                }

            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao realizar processamento")
            } finally {
                urlConnection?.disconnect()
            }

            return response // ✅ Agora está acessível aqui
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("Preço ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("Bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("Potencia ->", potencia)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("Recarga ->", recarga)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("urlPhoto ->", urlPhoto)

                    val model = Carro (
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto,
                        isFavorite = false

                    )
                    carrosArray.add(model)

                }
                progress.isVisible = false
                noInternetText.isVisible = false
                noInternetImage.isVisible = false
                setupList()


            } catch (ex: Exception) {

            }
        }





        fun streamToString(inputStream: InputStream) : String {

            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line:String

            var result = ""

            try {
                do {
                    line = bufferReader.readLine()
                    line?.let {
                        result += line
                    }
                } while (line!= null)
            } catch (ex: Exception) {
                Log.e("Erro", "Erro Stream")

            }
            return result
        }
    }
    fun checkForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val netWorkInfo = connectivityManager.activeNetworkInfo ?: return false
            return netWorkInfo.isConnected
        }
    }



}