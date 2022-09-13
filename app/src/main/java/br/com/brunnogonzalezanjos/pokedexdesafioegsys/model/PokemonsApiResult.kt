package br.com.brunnogonzalezanjos.pokedexdesafioegsys.model

data class PokemonsApiResult(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String,
)

data class PokemonApiResult(
    val id: Long,
    val weight: Int,
    val height: Int,
    val name: String,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStats>,
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

// [0] HP | [1] Attack | [2]Defence
data class PokemonStats(
    val base_stat: Int,
    val stat: PokemonStat
)

data class PokemonStat(
    val name: String
)
