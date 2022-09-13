package br.com.brunnogonzalezanjos.pokedexdesafioegsys.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.R
import br.com.brunnogonzalezanjos.pokedexdesafioegsys.model.Pokemon
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.item_card_pokemon.view.*

class PokemonListAdapter(
    private val context: Context,
    private val item: MutableList<Pokemon?> = mutableListOf(),
    var onItemClick: (pokemon: Pokemon) -> Unit = {}
) : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView = LayoutInflater.from(context)
            .inflate(
                R.layout.item_card_pokemon,
                parent,
                false
            )
        return ViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = item[position]
        holder.binding(item)
    }

    override fun getItemCount() = item.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var pokemon: Pokemon

        init {
            itemView.setOnClickListener {
                if (::pokemon.isInitialized) {
                    onItemClick(pokemon)
                }
            }
        }

        fun binding(item: Pokemon?) {

            item?.let { it ->
                this.pokemon = item
                Glide.with(itemView.context).load(it.imageUrl).into(itemView.ivPokemon)
                itemView.tvNumberPokemon.text = "Nº ${item.formattedNumber}"
                itemView.tvNamePokemon.text = item.name.replaceFirstChar { it.uppercase() }
                itemView.tvElementPokemon.text =
                    item.types[0].name.replaceFirstChar { it.uppercase() }

                if (item.types.size > 1) {
                    itemView.tvElementPokemon2.visibility = View.VISIBLE
                    itemView.tvElementPokemon2.text =
                        item.types[1].name.replaceFirstChar { it.uppercase() }
                } else {
                    itemView.tvElementPokemon2.visibility = View.GONE
                }
            }
        }
    }
}