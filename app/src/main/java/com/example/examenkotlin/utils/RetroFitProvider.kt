package com.example.examenkotlin.utils

import com.example.examenkotlin.services.PeliculasServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitProvider {
    companion object {
        fun getRetroFit(): PeliculasServices {
            val retroFit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retroFit.create(PeliculasServices::class.java)
        }
    }
}