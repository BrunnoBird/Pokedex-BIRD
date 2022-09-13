package br.com.brunnogonzalezanjos.pokedexdesafioegsys.utils

import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonResult

fun randomPokemonById(it: List<Pokemon?>): Pokemon? {
    val randomPokemonId = (0..it.size).random()
    val randomPokemon = it[randomPokemonId]
    return randomPokemon
}

fun extractIdPokemon(pokemonResult: PokemonResult): Long {
    val number = pokemonResult.url
        .replace("https://pokeapi.co/api/v2/pokemon/", "")
        .replace("/", "").toLong()
    return number
}