package com.codidevs.nutriapp.data.models

/** Un nutriente con su emoji, nombre, beneficio y ejemplos de alimentos. */
data class Nutriente(
    val emoji: String,
    val nombre: String,
    val beneficio: String,
    val ejemplos: List<String>
)

/**
 * Catálogo de nutrientes del Nivel 2 (Actividad: "Descubre los nutrientes").
 * Extraído de la tabla "Grupo de alimentos / Nutriente principal / Beneficio" de las indicaciones.
 */
object CatalogoNutrientes {
    val TODOS = listOf(
        Nutriente("🍎", "Vitaminas", "Protegen de enfermedades y ayudan a la digestión", listOf("Manzana", "Naranja", "Mango", "Banano")),
        Nutriente("🥦", "Fibra", "Ayudan a la digestión y al crecimiento", listOf("Brócoli", "Zanahoria", "Espinaca")),
        Nutriente("🍞", "Carbohidratos", "Proporcionan energía para jugar y aprender", listOf("Arroz", "Pan", "Papa", "Pasta")),
        Nutriente("🍗", "Proteínas", "Forman músculos y ayudan al crecimiento", listOf("Pollo", "Carne", "Pescado", "Huevo")),
        Nutriente("🥛", "Calcio", "Fortalecen huesos y dientes", listOf("Leche", "Queso", "Yogur")),
        Nutriente("🥑", "Grasas saludables", "Protegen el corazón y el cerebro", listOf("Aguacate", "Aceite de oliva", "Nueces")),
        Nutriente("🍬", "Azúcares", "Dan energía rápida, pero con moderación", listOf("Dulces", "Chocolates", "Gaseosas"))
    )
}
