package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
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
        fetchPokemons()
    }

    private fun listeners() {
        searchPokemonListener()
        fabListener()
        radioButtonListener()
        closeKeyboard()
    }

    private fun fetchPokemons() {
        pb_load_pokemons.visibility = View.VISIBLE
        viewModel.pokemons.observe(this, Observer {
            loadRecyclerView(it as MutableList<Pokemon?>)
        })
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

    private fun fabListener() {
        fab_random_pokemon.setOnClickListener {
            randomPokemon()
        }
    }

    private fun randomPokemon() {
        val randomPokemon = viewModel.randomPokemon()
        randomPokemon?.let {
            openPokemonDetails(randomPokemon)
        }
    }

    private fun openPokemonDetails(it: Pokemon) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(POKEMON_ID, it.number)
        startActivity(intent)
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

    private fun searchPokemonListener() {
        input_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                return
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(name: CharSequence?, start: Int, before: Int, count: Int) {
                when (typeFilter) {
                    FILTER_TYPE_NAME -> {
                        name?.let {
                            val filteredPokemon = viewModel.filterByName(name)
                            filteredPokemon?.let {
                                loadRecyclerView(it.toMutableList())
                            }
                        }
                    }
                    FILTER_TYPE_TYPE -> {
                        name?.let {
                            val filteredPokemons = viewModel.filterByType(name)
                            filteredPokemons?.let {
                                loadRecyclerView(it.toMutableList())
                            }
                        }
                    }
                }
            }
        })
        input_search.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    private fun closeKeyboard() {
        activity_main_root.setOnClickListener {
            hideKeyboard(it)
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        input_search.clearFocus()
    }
}
