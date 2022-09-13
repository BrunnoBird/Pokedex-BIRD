package br.com.brunnogonzalezanjos.pokedexdesafioegsys.model

data class Pokemon(
    val number: Long,
    val name: String,
    val types: List<PokemonType>,
    val attack: Int,
    val defense: Int,
    val hp: Int,
    var weight: Int,
    var height: Int,
) {
    val formattedName = name.replaceFirstChar { it.uppercase() }
    val formattedNumber = number.toString().padStart(3, '0')
    val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
}
