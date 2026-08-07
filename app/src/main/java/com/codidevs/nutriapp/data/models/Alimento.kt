package com.codidevs.nutriapp.data.models

/** Un alimento con su emoji, nombre y el beneficio que aporta al cuerpo. */
data class Alimento(
    val emoji: String,
    val nombre: String,
    val beneficio: String
)

/**
 * Catálogo de alimentos del Nivel 1 (Actividad: "Descubre los alimentos").
 * Extraído de la tabla "Dibujo / Alimento / ¿Qué hace en tu cuerpo?" de las indicaciones.
 */
object CatalogoAlimentos {
    val TODOS = listOf(
        Alimento("🍎", "Manzana", "Me da vitaminas para no enfermarme"),
        Alimento("🥕", "Zanahoria", "Ayuda a cuidar mis ojos"),
        Alimento("🥦", "Brócoli", "Me protege de enfermedades"),
        Alimento("🍌", "Banano", "Me da energía para jugar"),
        Alimento("🥛", "Leche", "Fortalece mis huesos y dientes"),
        Alimento("🧀", "Queso", "Ayuda a crecer fuerte"),
        Alimento("🥚", "Huevo", "Forma músculos"),
        Alimento("🍗", "Pollo", "Construye músculos y tejidos"),
        Alimento("🥩", "Carne", "Me ayuda a crecer fuerte"),
        Alimento("🐟", "Pescado", "Ayuda al cerebro y al corazón"),
        Alimento("🍚", "Arroz", "Me da energía"),
        Alimento("🍞", "Pan", "Me da energía para estudiar"),
        Alimento("🥔", "Papa", "Me da energía"),
        Alimento("🌽", "Maíz", "Me da energía"),
        Alimento("🫘", "Fríjoles", "Me ayudan a crecer y tienen fibra"),
        Alimento("🥜", "Maní", "Tiene grasas saludables y proteínas"),
        Alimento("🫒", "Aceite de oliva", "Protege mi corazón"),
        Alimento("🧈", "Mantequilla", "Me da energía, pero debo comer poca"),
        Alimento("🍯", "Miel", "Me da energía rápida"),
        Alimento("🍬", "Dulces", "Mucha azúcar; solo de vez en cuando")
    )
}
