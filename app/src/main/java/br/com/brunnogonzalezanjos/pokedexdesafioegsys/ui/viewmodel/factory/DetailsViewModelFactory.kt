package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.DetailsViewModel

class DetailsViewModelFactory(
    private val id: Long,
    private val repository: PokemonRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(id, repository) as T
    }

}