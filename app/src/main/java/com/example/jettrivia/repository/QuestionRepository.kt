package com.example.jettrivia.repository

import android.util.Log
import com.example.jettrivia.data.DataorException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import java.util.ArrayList
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api:QuestionApi) {
    private val dataorException= DataorException<ArrayList<QuestionItem>,
            Boolean,
            Exception>()
    suspend fun getAllQuestions():DataorException<ArrayList<QuestionItem>,Boolean,java.lang.Exception>{
        try {
            dataorException.loading=true
            dataorException.data=api.getAllQuestions()
            if (dataorException.data.toString().isNotEmpty()) dataorException.loading=false
        }
        catch (exception:Exception){
            dataorException.e=exception
            Log.d("Exc","get All Questions:${dataorException.e!!.localizedMessage}")
        }
        return dataorException
    }

}