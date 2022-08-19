package com.example.jettrivia.network

import com.example.jettrivia.model.Question
import javax.inject.Singleton
import retrofit2.http.GET

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getAllQuestions():Question
}