package com.example.examenkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examenkotlin.R
import com.example.examenkotlin.data.FichaPelicula
import com.example.examenkotlin.data.PeliculaClass
import com.example.examenkotlin.databinding.ActivityPeliculasBinding
import com.example.examenkotlin.utils.RetroFitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PeliculasActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PELICULA_ID = "EXTRA_PELICULA_ID"
    }

    lateinit var binding: ActivityPeliculasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityPeliculasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var peliculaID = intent.getStringExtra(EXTRA_PELICULA_ID)!!

        LoadPeliculaFullAPI(peliculaID)
    }

//--------------------------------------------------------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
//--------------------------------------------------------------------------------------------------

    private fun LoadPeliculaFullAPI(peliculaID: String) {

        val service = RetroFitProvider.getRetroFit()
        var peliculaFicha: FichaPelicula

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = service.PeliculaFullResponse(peliculaID)

                CoroutineScope(Dispatchers.Main).launch {
                    println(result)
                    if (result.response == "True") {
                        peliculaFicha = result
                        LoadData(peliculaFicha)
                    } else {
                        // Toast.makeText(this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }

// Cargamos los elementos de la ficha de la película  ----------------------------------------------
    fun LoadData(pelicula: FichaPelicula) {

        with (binding) {
            tituloTextView.text = pelicula.title
            anioTextView.text = pelicula.year
            sinopsisTextView.text = pelicula.plot
            duracionTextView.text = pelicula.runtime
            directorTextView.text = pelicula.director
            generoTextView.text = pelicula.genre
            paisTextView.text = pelicula.country

            Picasso.get().load(pelicula.poster).into(binding.peliculaImageView)
        }
    }
}