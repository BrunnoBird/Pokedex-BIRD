package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.databinding.ActivityDetailsBinding
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.repository.PokemonRepository
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.DetailsViewModel
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.viewmodel.factory.DetailsViewModelFactory
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private val pokemonId: Long by lazy {
        intent.getLongExtra(POKEMON_ID, 0)
    }

    private val viewModel by lazy {
        val repository = PokemonRepository
        val factory = DetailsViewModelFactory(pokemonId, repository)
        ViewModelProvider(this, factory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding()
        listeners()
    }

    private fun listeners() {
        val buttonBack = binding.pokemonDetailsToolbarBack.ibBack
        buttonBack.setOnClickListener { finish() }
    }

    private fun binding() {
        viewModel.pokemon.observe(this, Observer {
            it?.let {
                Glide.with(binding.ivPokemonImageDetail.context).load(it.imageUrl)
                    .into(binding.ivPokemonImageDetail)
                binding.tvPokemonNameDetail.text = it.name.replaceFirstChar { it.uppercase() }
                binding.tvNumberPokemonDetail.text = "#${it.number}"
                binding.tvHeightPokemonDetailsValue.text = "${it.height} M"
                binding.tvWeightPokemonDetailsValue.text = "${it.weight} Kg"
                binding.tvPokemonDetailHpPoints.text = it.hp.toString()
                binding.progressBarPokemonDetailHp.progress = it.hp
                binding.tvPokemonDetailAtkPoints.text = it.attack.toString()
                binding.progressBarPokemonDetailAtk.progress = it.attack
                binding.tvPokemonDetailDefPoints.text = it.defense.toString()
                binding.progressBarPokemonDetailDef.progress = it.defense

                binding.tvPokemonDetatilElement.text =
                    it.types[0].name.replaceFirstChar { it.uppercase() }
                if (it.types.size > 1) {
                    binding.tvPokemonDetatilElement2.visibility = View.VISIBLE
                    binding.tvPokemonDetatilElement2.text =
                        it.types[1].name.replaceFirstChar { it.uppercase() }
                } else {
                    binding.tvPokemonDetatilElement2.visibility = View.GONE
                }
            }
        })
    }
}