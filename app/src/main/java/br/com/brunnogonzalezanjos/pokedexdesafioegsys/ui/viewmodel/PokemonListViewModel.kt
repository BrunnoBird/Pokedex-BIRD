package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.PokemonResult
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    var pokemons = MutableLiveData<List<Pokemon?>>()

    init {
        Thread(Runnable {
            loadPokemons()
        }).start()
    }

    private fun loadPokemons() {
        val pokemonsApiResult = repository.listPokemons()

        pokemonsApiResult?.results?.let {
            pokemons.postValue(it.map { pokemonResult ->
                val number = extractIdPokemon(pokemonResult)

                val pokemonApiResult = repository.getPokemon(number)

                pokemonApiResult?.let {
                    Pokemon(
                        number = pokemonApiResult.id,
                        weight = pokemonApiResult.weight,
                        height = pokemonApiResult.height,
                        name = pokemonApiResult.name,
                        types = pokemonApiResult.types.map { type ->
                            type.type
                        },
                        hp = pokemonApiResult.stats[0].base_stat,
                        attack = pokemonApiResult.stats[1].base_stat,
                        defense = pokemonApiResult.stats[2].base_stat
                    )
                }
            })
        }
    }

    private fun extractIdPokemon(pokemonResult: PokemonResult): Long {
        val number = pokemonResult.url
            .replace("https://pokeapi.co/api/v2/pokemon/", "")
            .replace("/", "").toLong()
        return number
    }

}