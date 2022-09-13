package br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit.service

import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonApiResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonsApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int): Call<PokemonsApiResult>

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Long): Call<PokemonApiResult>
}