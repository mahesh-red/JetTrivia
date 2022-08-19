package com.example.jettrivia.component

import android.app.Notification
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.Question
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel){
    val questions=viewModel.data.value.data?.toMutableList()
    val questionIndex= remember { mutableStateOf(0) }
    if(viewModel.data.value.loading==true){
//        Text("Loading")
        CircularProgressIndicator()
        Log.d("Question","Questions Loading")
    }
    else{
        val question=try {
                questions?.get(questionIndex.value)
            } catch (exc:Exception){exc}

        if (questions!=null){

            QuestionsDisplay(question = question!! as QuestionItem, viewModel = viewModel, questionIndex = questionIndex){
                questionIndex.value=questionIndex.value+1
            }
        }

    }

}


//@Preview
@Composable
fun QuestionsDisplay(question:QuestionItem,
questionIndex:MutableState<Int>,
viewModel: QuestionsViewModel,
onNextClicked:(Int)->Unit={}
){
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    val choicesState = remember(question) {
        question.choices.toMutableList()

    }
    val answerState=remember{ mutableStateOf<Int?>(null) }
    val correctAnswerState= remember(question) {
        mutableStateOf<Boolean?>(null)


    }
    val updateAnswer:(Int)-> Unit = remember(question){
        {
            answerState.value = it
            correctAnswerState.value=choicesState[it]==question.answer

        }
    }

    val total_questions:Int=viewModel.getTotalQuestionCount()
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), color = AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            QuestionTracker(count = questionIndex.value, total = total_questions)

            DrawDottedLine(pathEffect)

            Column {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp)

                //choices
                choicesState.forEachIndexed { index, answerText ->
                Row(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .border(
                        width = 3.dp, brush = Brush.linearGradient(
                            colors = listOf(AppColors.mOffDarkPurple, AppColors.mOffDarkPurple)
                        ),
                        shape = RoundedCornerShape(
                            topStartPercent = 50,
                            topEndPercent = 50,
                            bottomEndPercent = 50,
                            bottomStartPercent = 50
                        )


                    )
                    .background(color = Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically
                )
                 {
                     Log.d("hello mahesh","to much head ache5")
                    RadioButton(selected = (answerState.value==index),
                        onClick = { updateAnswer(index) }, modifier =Modifier.padding(start = 16.dp),
                    colors = RadioButtonDefaults.colors(selectedColor = if (correctAnswerState.value==true &&
                            index == answerState.value){
                        Color.Green.copy(0.2f)
                    }
                    else{
                        Color.Red.copy(0.2f)
                    }
                    )
                    )//Radio button ends
                    val annotatedString = buildAnnotatedString {
                        withStyle(
                        style = SpanStyle(color = if (correctAnswerState.value==true &&
                            index == answerState.value){
                            Color.Green
                        }
                        else if(correctAnswerState.value==false &&
                            index == answerState.value){
                            Color.Red
                     }
                                else { AppColors.mOffWhite }, fontSize = 17.sp))
                        {
                            //Text(answerText)
                            append((answerText))
                        }
                }
                     Text(text = annotatedString, modifier = Modifier.padding(10.dp))


                     }


            }
                Button(onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(4.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.mLightBlue)


                ) {
                    Text(text = "Next",
                        modifier = Modifier.padding(5.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }

        }

    }
}

//@Preview
@Composable
fun QuestionTracker(count:Int,total:Int=100){
    Text(text = buildAnnotatedString { withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
        withStyle(style = SpanStyle(color = AppColors.mLightGray,
        fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )){append("Question:$count/")}

        withStyle(style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Light,
            fontSize = 15.sp
        )){append("$total")}
    } },
    modifier = Modifier.padding(20.dp))


}


@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    androidx.compose.foundation.Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp) ){
        drawLine(color = AppColors.mLightGray,
        start = Offset(0f,0f),
        end = Offset(size.width,0f),
        pathEffect=pathEffect)
    }

}