package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.databinding.ActivityMainBinding
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.retrofit.AppRetrofit
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.recyclerview.PokemonListAdapter
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.PokemonListViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.factory.PokemonListViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var typeFilter: String = FILTER_TYPE_NAME
    private val viewModel by lazy {
        val service = AppRetrofit().pokemonService
        val repository = PokemonRepository(service)
        val factory = PokemonListViewModelFactory(repository)
        ViewModelProvider(this, factory).get(PokemonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.pbLoadPokemons.visibility = View.VISIBLE
        viewModel.pokemons.observe(this, Observer {
            loadRecyclerView(it as MutableList<Pokemon?>)
        })
    }

    private fun loadRecyclerView(
        pokemons: MutableList<Pokemon?>,
    ) {
        binding.activityListPokemonRecyclerview.adapter = PokemonListAdapter(
            item = pokemons,
            onItemClick = this::openPokemonDetails
        )
        binding.pbLoadPokemons.visibility = View.GONE
    }

    private fun fabListener() {
        binding.fabRandomPokemon.setOnClickListener {
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
        binding.rgFilterActivity.setOnCheckedChangeListener { _, i ->
            when (findViewById<RadioButton>(i)) {
                binding.radioName ->
                    if (binding.radioName.isChecked) {
                        typeFilter = FILTER_TYPE_NAME
                    }
                binding.radioType ->
                    if (binding.radioType.isChecked) {
                        typeFilter = FILTER_TYPE_TYPE
                    }
            }
        }
    }

    private fun searchPokemonListener() {
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
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
                            loadRecyclerView(filteredPokemon.toMutableList())
                        }
                    }
                    FILTER_TYPE_TYPE -> {
                        name?.let {
                            val filteredPokemons = viewModel.filterByType(name)
                            loadRecyclerView(filteredPokemons.toMutableList())
                        }
                    }
                }
            }
        })
//        binding.inputSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    private fun closeKeyboard() {
        binding.root.setOnClickListener {
            hideKeyboard(it)
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        binding.inputSearch.clearFocus()
    }
}
