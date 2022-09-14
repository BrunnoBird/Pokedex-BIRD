package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.utils.extractIdPokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.utils.randomPokemonById

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private var _pokemons = MutableLiveData<List<Pokemon?>>()
    val pokemons: LiveData<List<Pokemon?>> = _pokemons

    init {
        Thread {
            loadPokemons()
        }.start()
    }

    private fun loadPokemons() {
        val pokemonsApiResult = repository.listPokemons()

        pokemonsApiResult?.results?.let {
            _pokemons.postValue(it.map { pokemonResult ->
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

    fun randomPokemon(): Pokemon? {

        if (!pokemons.value.isNullOrEmpty()) {
            pokemons.value.let {
                it?.let {
                    val randomPokemon = randomPokemonById(it)
                    return randomPokemon
                }
            }
        }
        return null
    }

    fun filterByName(name: CharSequence): List<Pokemon?> {

        val listFiltered = pokemons.value?.filter {
            it?.name?.contains(name.toString().lowercase()) ?: false
        }
        listFiltered?.let {
            return it
        }
        return listOf<Pokemon>()
    }

    fun filterByType(pokemonType: CharSequence): List<Pokemon?> {

        val listFiltered = pokemons.value?.filter {
            val typesPokemon = it?.types?.map { type -> type.name }
            typesPokemon?.map { type -> type }?.contains(pokemonType.toString().lowercase())
                ?: false
        }
        listFiltered?.let {
            return it
        }
        return listOf<Pokemon>()
    }

}