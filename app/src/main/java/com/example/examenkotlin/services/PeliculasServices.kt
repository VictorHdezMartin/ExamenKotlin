package com.example.examenkotlin.services

import com.example.examenkotlin.data.FichaPelicula
import com.example.examenkotlin.data.PeliculaClass
import com.example.examenkotlin.data.PeliculasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculasServices {

//  llamada para obtener todas las peliculas   (OK) -----------------------------------------------
    @GET("?apikey=fb7aca4")
    suspend fun PeliculaResponse(@Query("s") query: String): PeliculasResponse

// llamada para obtener un Pelicula por ID  (OK)  --------------------------------------------------
    @GET("?apikey=fb7aca4")
    suspend fun PeliculaFullResponse(@Query("i") query: String): FichaPelicula

}

