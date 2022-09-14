package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository

class DetailsViewModel(id: Long, private val repository: PokemonRepository) : ViewModel() {
    private var _pokemon = MutableLiveData<Pokemon?>()
    val pokemon: LiveData<Pokemon?> = _pokemon

    init {
        Thread {
            getPokemonById(id)
        }.start()
    }

    private fun getPokemonById(id: Long) {
        val pokemonApiResult = repository.getPokemon(id)

        pokemonApiResult?.let {
            _pokemon.postValue(
                Pokemon(
                    number = it.id,
                    weight = it.weight,
                    height = it.height,
                    name = it.name,
                    types = it.types.map { type ->
                        type.type
                    },
                    hp = it.stats[0].base_stat,
                    attack = it.stats[1].base_stat,
                    defense = it.stats[2].base_stat
                )
            )
        }
    }
}