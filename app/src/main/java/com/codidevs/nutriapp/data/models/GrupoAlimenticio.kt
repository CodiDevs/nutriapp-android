package com.codidevs.nutriapp.data.models

/** Un alimento con su emoji y nombre (para mostrar la figura). */
data class AlimentoConEmoji(
    val emoji: String,
    val nombre: String
)

/** Un grupo alimenticio con su emoji, nombre y los alimentos (con emoji) que lo componen. */
data class GrupoAlimenticio(
    val emoji: String,
    val nombre: String,
    val alimentos: List<AlimentoConEmoji>
)

/**
 * Los 8 grupos de la tabla "Grupo / Alimentos" de las indicaciones.
 * Se usa en la actividad "¿A qué grupo pertenece?" (arrastrar el alimento a su grupo).
 */
object GruposAlimenticios {
    val TODOS = listOf(
        GrupoAlimenticio(
            "🍎", "Frutas",
            listOf(
                AlimentoConEmoji("🍎", "Manzana"),
                AlimentoConEmoji("🍌", "Banano"),
                AlimentoConEmoji("🍊", "Naranja"),
                AlimentoConEmoji("🍈", "Papaya"),
                AlimentoConEmoji("🥭", "Mango"),
                AlimentoConEmoji("🍉", "Sandía")
            )
        ),
        GrupoAlimenticio(
            "🥦", "Verduras",
            listOf(
                AlimentoConEmoji("🥕", "Zanahoria"),
                AlimentoConEmoji("🍅", "Tomate"),
                AlimentoConEmoji("🥬", "Lechuga"),
                AlimentoConEmoji("🥒", "Pepino"),
                AlimentoConEmoji("🍃", "Espinaca"),
                AlimentoConEmoji("🥦", "Brócoli")
            )
        ),
        GrupoAlimenticio(
            "🍞", "Cereales y harinas",
            listOf(
                AlimentoConEmoji("🍚", "Arroz"),
                AlimentoConEmoji("🍞", "Pan"),
                AlimentoConEmoji("🥔", "Papa"),
                AlimentoConEmoji("🍠", "Yuca"),
                AlimentoConEmoji("🍝", "Pasta"),
                AlimentoConEmoji("🌾", "Avena"),
                AlimentoConEmoji("🌽", "Maíz")
            )
        ),
        GrupoAlimenticio(
            "🍗", "Proteínas",
            listOf(
                AlimentoConEmoji("🍗", "Pollo"),
                AlimentoConEmoji("🥩", "Carne"),
                AlimentoConEmoji("🐟", "Pescado"),
                AlimentoConEmoji("🥚", "Huevo")
            )
        ),
        GrupoAlimenticio(
            "🫘", "Legumbres",
            listOf(
                AlimentoConEmoji("🫘", "Fríjoles"),
                AlimentoConEmoji("🟤", "Lentejas"),
                AlimentoConEmoji("🟡", "Garbanzos"),
                AlimentoConEmoji("🫛", "Arvejas")
            )
        ),
        GrupoAlimenticio(
            "🥛", "Lácteos",
            listOf(
                AlimentoConEmoji("🥛", "Leche"),
                AlimentoConEmoji("🧀", "Queso"),
                AlimentoConEmoji("🫗", "Yogur")
            )
        ),
        GrupoAlimenticio(
            "🥜", "Grasas saludables",
            listOf(
                AlimentoConEmoji("🥑", "Aguacate"),
                AlimentoConEmoji("🌰", "Nueces"),
                AlimentoConEmoji("🥜", "Maní"),
                AlimentoConEmoji("🫒", "Aceite de oliva")
            )
        ),
        GrupoAlimenticio(
            "🍬", "Azúcares",
            listOf(
                AlimentoConEmoji("🥤", "Gaseosas"),
                AlimentoConEmoji("🍬", "Dulces"),
                AlimentoConEmoji("🍫", "Chocolates"),
                AlimentoConEmoji("🍪", "Galletas"),
                AlimentoConEmoji("🍦", "Helados")
            )
        )
    )
}
