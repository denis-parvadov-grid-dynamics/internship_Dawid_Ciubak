package com.example.data.model
import com.example.domain.model.Model

data class ModelDto(
    val holder: String? = null
)

// extension function to transform a dto into a desired model
fun ModelDto.toModel(): Model {
    return Model(holder = this.holder)
}
