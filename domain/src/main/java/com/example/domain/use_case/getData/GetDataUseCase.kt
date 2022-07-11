package com.example.domain.use_case.getData

import com.example.domain.common.Resource
import com.example.domain.model.Model
import com.example.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Resource<List<Model>>> = flow{
        try {
            emit(Resource.Loading())
            val data = repository.getData()
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "unknown exception"))
        } catch (e: IOException) {
            emit(Resource.Error("check your internet connection"))
        }
    }
}