package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.R
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.recyclerview.PokemonListAdapter
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.PokemonListViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.factory.PokemonListViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var typeFilter: String = FILTER_TYPE_NAME
    private val viewModel by lazy {
        val repository = PokemonRepository
        val factory = PokemonListViewModelFactory(repository)
        ViewModelProvider(this, factory).get(PokemonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listeners()
        findPokemons()
    }

    private fun listeners() {
        searchPokemonListener()
        fabListener()
        radioButtonListener()
    }

    private fun radioButtonListener() {
        rgFilterActivity.setOnCheckedChangeListener { _, i ->
            when (findViewById<RadioButton>(i)) {
                radio_name ->
                    if (radio_name.isChecked) {
                        typeFilter = FILTER_TYPE_NAME
                    }
                radio_type ->
                    if (radio_type.isChecked) {
                        typeFilter = FILTER_TYPE_TYPE
                    }
            }
        }
    }

    private fun fabListener() {
        fab_random_pokemon.setOnClickListener {
            randomPokemon()
        }
    }

    private fun randomPokemon() {
        hasPokemon()
    }

    private fun hasPokemon() {
        if (viewModel.pokemons.value?.isNotEmpty() == true) {
            viewModel.pokemons.observe(this, Observer {
                it?.let {
                    val randomPokemon = randomPokemonById(it)
                    randomPokemon?.let {
                        openPokemonDetails(randomPokemon)
                    }
                }
            })
        } else {
            return
        }
    }

    private fun randomPokemonById(it: List<Pokemon?>): Pokemon? {
        val randomPokemonId = (0..it.size).random()
        val randomPokemon = it[randomPokemonId]
        return randomPokemon
    }

    private fun loadRecyclerView(
        pokemons: MutableList<Pokemon?>,
    ) {
        activity_list_pokemon_recyclerview.adapter = PokemonListAdapter(
            this,
            item = pokemons,
            onItemClick = this::openPokemonDetails
        )
        pb_load_pokemons.visibility = View.GONE
    }

    private fun openPokemonDetails(it: Pokemon) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(POKEMON_ID, it.number)
        startActivity(intent)
    }

    private fun findPokemons() {
        pb_load_pokemons.visibility = View.VISIBLE
        viewModel.pokemons.observe(this, Observer {
            loadRecyclerView(it as MutableList<Pokemon?>)
        })
    }

    private fun searchPokemonListener() {
        input_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (typeFilter) {
                    FILTER_TYPE_NAME -> {
                        val pokemons =
                            viewModel.pokemons.value?.filter {
                                it?.name?.contains(s.toString().lowercase()) ?: false
                            }
                        pokemons?.let {
                            loadRecyclerView(it.toMutableList())
                        }
                    }
                    FILTER_TYPE_TYPE -> {
                        val pokemons =
                            viewModel.pokemons.value?.filter {
                                val typesPoke = it?.types?.map { type -> type.name }
                                typesPoke?.map { type -> type }
                                    ?.contains(s.toString().lowercase()) ?: false
                            }
                        pokemons?.let {
                            loadRecyclerView(it.toMutableList())
                        }
                    }
                }
            }
        })
    }
}
