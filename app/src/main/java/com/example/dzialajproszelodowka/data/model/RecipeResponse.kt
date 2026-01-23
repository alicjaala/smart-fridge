package com.example.dzialajproszelodowka.data.model

// dto opisuje jak wyglądają jsony które dostarcza api
data class RecipeListResponse(
    val results: List<RecipeDto>
)

data class RecipeDto(
    val id: Int,
    val title: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val image: String?,

    val analyzedInstructions: List<InstructionDto>? = null,

    val instructions: String? = null,
    val summary: String? = null,

    val extendedIngredients: List<IngredientDto> = emptyList()
)

data class InstructionDto(
    val steps: List<StepDto>
)

data class StepDto(
    val number: Int,
    val step: String
)

data class IngredientDto(
    val original: String
)