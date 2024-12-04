package com.example.examenkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.examenkotlin.R
import com.example.examenkotlin.adapters.MainAdapter
import com.example.examenkotlin.data.PeliculaClass
import com.example.examenkotlin.databinding.ActivityMainBinding
import com.example.examenkotlin.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.examenkotlin.utils.RetroFitProvider

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainAdapter
    var peliculasList: List<PeliculaClass> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)                                           // icono de pantalla anterior en el AcctionBar

        adapter = MainAdapter(peliculasList, { position ->
            val pelicula = peliculasList[position]
            navigateToPelicula(pelicula, position)
        })

        binding.MainRecyclerView.adapter = adapter
        binding.MainRecyclerView.layoutManager = GridLayoutManager(this, 2)  // nº columnas
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)

        val menuItem = menu?.findItem(R.id.menu_buscarPelicula)!!
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                BuscarPelicula(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

//--------------------------------------------------------------------------------------------------

    private fun BuscarPelicula(query: String) {
        val service = RetroFitProvider.getRetroFit()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = service.PeliculaResponse(query, Constants.tokenOMDBAPI)

                CoroutineScope(Dispatchers.Main).launch {
                    if (result.response == "True") {
                        peliculasList = result.search
                        adapter.updateItems(peliculasList)
                    } else {
                        // Toast.makeText(this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }

// Vamos al activity de Productos, pasando el parametro de busqueda de los productos de la categoria
    private fun navigateToPelicula(pelicula: PeliculaClass, position: Int) {
        val intent = Intent(this, PeliculasActivity::class.java)
        intent.putExtra(PeliculasActivity.EXTRA_PELICULA_ID, pelicula.imdbID)
        startActivity(intent)
    }
}