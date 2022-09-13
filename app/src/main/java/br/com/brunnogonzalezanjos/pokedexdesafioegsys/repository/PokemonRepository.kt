package br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository

import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonApiResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonsApiResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit.service.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val service: PokemonService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonService::class.java);
    }

    fun listPokemons(limit: Int = 151): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()
    }

    fun getPokemon(id: Long): PokemonApiResult? {
        val call = service.getPokemon(id)

        return call.execute().body()
    }
}