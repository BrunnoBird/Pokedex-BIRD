package br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository

import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonApiResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonsApiResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit.service.PokemonService

class PokemonRepository(private val service: PokemonService) {

    fun listPokemons(limit: Int = 151): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()
    }

    fun getPokemon(id: Long): PokemonApiResult? {
        val call = service.getPokemon(id)

        return call.execute().body()
    }
}