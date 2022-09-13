package br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit

import br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit.service.PokemonService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL: String = "https://pokeapi.co/api/v2/"

class AppRetrofit {

    private val client by lazy {
        val interceptador = HttpLoggingInterceptor()
        interceptador.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(interceptador)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val pokemonService: PokemonService by lazy {
        retrofit.create(PokemonService::class.java);
    }
}