 package com.example.composecourse

import android.annotation.SuppressLint
import android.health.connect.datatypes.units.Percentage
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.composecourse.ui.theme.ComposeCourseTheme
import kotlinx.coroutines.launch
import kotlin.random.Random
 import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DraggableMusicKnob()
        }
    }
}

 @OptIn(ExperimentalComposeUiApi::class)
 @Composable
fun MusicKnob(
     modifier: Modifier = Modifier,
     limitingAngle : Float = 25f,
     onValueChange : (Float) -> Unit
      ){
     var rotation by remember{
        mutableStateOf(limitingAngle)
     }
     var touchX by remember {
         mutableStateOf(0f)
     }
     var touchY by remember {
         mutableStateOf(0f)
     }
     var centreX by remember {
         mutableStateOf(0f)
     }
     var centreY by remember {
         mutableStateOf(0f)
     }

     Image(
         painter = painterResource(id = R.drawable.music_knob),
         contentDescription = "Music Knob",
         modifier = modifier
             .fillMaxSize()
             .onGloballyPositioned {
                 val windowBounds = it.boundsInWindow()
                 centreX = windowBounds.size.width / 2f
                 centreY = windowBounds.size.height / 2f
             }
             .pointerInteropFilter { event ->
                 touchX = event.x
                 touchY = event.y

                 val angle = -atan2(centreX - touchX, centreY - touchY) * (180f / PI).toFloat()

                 when (event.action) {
                     MotionEvent.ACTION_DOWN,
                     MotionEvent.ACTION_MOVE -> {
                         if (angle !in -limitingAngle..limitingAngle) {
                             val fixedAngle = if (angle in -180f..-limitingAngle) {
                                 360f + angle
                             } else {
                                 angle
                             }

                             rotation = fixedAngle

                             val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                             onValueChange(percent)
                             true
                         } else {
                             false
                         }
                     }

                     else -> false
                 }
             }
             .rotate(rotation)
         )
}

 @Composable
 fun VolumeBar(
     modifier: Modifier = Modifier,
     activeBars : Int = 0,
     barCount : Int = 10
 ){
     BoxWithConstraints(
         contentAlignment = Alignment.Center,
         modifier = modifier
     ) {
         val barWidth = remember {
             constraints.maxWidth / (2f * barCount)
         }
         Canvas(modifier = modifier){
             for (i in 0 until barCount){
                 drawRect(
                     color = if(i in 0 .. activeBars) Color.Green else Color.DarkGray,
                     topLeft = Offset(i * barWidth * 2f + barWidth / 2f , 0f),
                     size = Size(barWidth , constraints.maxHeight.toFloat()),
                 )
             }
         }
     }
 }

 @Composable
 fun DraggableMusicKnob(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
                .padding(30.dp)
        ){
            var volume by remember {
                mutableStateOf(0f)
            }
            val barCount = 20
            MusicKnob( modifier = Modifier
                .size(100.dp)
            ) {
                volume = it
            }

            Spacer(modifier = Modifier.width(20.dp))

            VolumeBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                activeBars = (barCount * volume).roundToInt(),
                barCount = barCount
            )
        }
    }
 }


 @Composable
 fun ApplyCircularProgressBar(){
     var percentage by remember {
         mutableStateOf(0f)
     }
     Box(contentAlignment = Alignment.Center,
         modifier = Modifier.fillMaxSize())
     {
         Column(modifier = Modifier.fillMaxSize()) {

             CircularProgressBar(percentage,100)

             Button(onClick = { percentage += 0.1f }) {
                 Text(text = "Increase")
             }

             Button(onClick = { percentage -= 0.1f }) {
                 Text(text = "Decrease")
             }
         }
     }
 }

 @Composable
 fun CircularProgressBar(
     percentage: Float,
     number: Int,
     fontSize : TextUnit = 28.sp,
     radius : Dp = 50.dp,
     color: Color = Color.Green,
     strokeWidth : Dp = 8.dp,
     animationDuration : Int = 1000,
     animDelay: Int = 0
 ){
     var animationPlayed by remember {
         mutableStateOf(false)
     }

     val currPercentage = animateFloatAsState(
         targetValue =  if(animationPlayed) percentage else 0f,
         animationSpec = tween(
             durationMillis = animationDuration,
             delayMillis = animDelay
         )
     )

     LaunchedEffect(key1 = true) {
         animationPlayed = true
     }

     Box(modifier = Modifier
         .size(radius * 2f),
         contentAlignment = Alignment.Center
     ){
         Canvas(modifier = Modifier.size(radius * 2f)){
            drawArc(
                color = color,
                -90f,
                360 * currPercentage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(),cap = StrokeCap.Round)
            )
         }

         Text(
             text = (currPercentage.value * number).toInt().toString(),
             color = Color.Black,
             fontSize = fontSize,
             fontWeight = FontWeight.Bold
         )

     }


 }

 @Composable
 fun SimpleAnimation(){
     var sizeState by remember {
         mutableStateOf(200.dp)
     }

     val size by animateDpAsState(
         targetValue = sizeState,
         tween(
             durationMillis = 3000,
             delayMillis = 500,
             easing = LinearOutSlowInEasing
         )
     )

     Box(modifier = Modifier
         .size(size)
         .background(Color.Red),
         contentAlignment = Alignment.Center){
         Column {
             Button(onClick = {
                 sizeState += 50.dp
             }) {
                 Text("Increase Size")
             }

             Button(onClick = {
                 sizeState -= 50.dp
             }) {
                 Text("Decrease Size")
             }
         }
     }

 }

 @Composable
 fun ConstraintLayoutfn(){
     val constraints = ConstraintSet {
         // Create references for the components
         val greenBox = createRefFor("greenBox")
         val redBox = createRefFor("redBox")
         val guideline = createGuidelineFromTop(0.5f)


         // Constrain the greenBox
         constrain(greenBox) {
             top.linkTo(guideline)
             start.linkTo(parent.start)
             width = Dimension.value(100.dp)
             height = Dimension.value(100.dp)
         }

         // Additional constraints can be added similarly
         constrain(redBox) {
             // Example constraints for redBox
             top.linkTo(parent.top)
             start.linkTo(greenBox.end)
             end.linkTo(parent.end)
             width = Dimension.value(100.dp)
             height = Dimension.value(100.dp)
         }

         createHorizontalChain(greenBox,redBox, chainStyle = ChainStyle.Packed)
     }

     ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()){
         Box(modifier = Modifier
             .background(Color.Green)
             .layoutId("greenBox")
         )

         Box(modifier = Modifier
             .background(Color.Red)
             .layoutId("redBox")
         )
     }
 }

 @Composable
 fun ImplementingList(value : Int){
//     val scrollState = rememberScrollState()
//     Column (
//         modifier =  Modifier.verticalScroll(scrollState)
//     ){
//         for( i in 1..value){
//             Text(
//                 text = "Item $i",
//                 fontSize = 24.sp,
//                 fontWeight = FontWeight.Bold,
//                 textAlign = TextAlign.Center,
//                 modifier = Modifier
//                     .fillMaxWidth()
//                     .padding(vertical = 24.dp)
//             )
//         }
//     }

     LazyColumn {
         itemsIndexed(
             listOf("This", "is", "jetpack","Compose")
         ){index, string ->
         Text(
                 text = string,
                 fontSize = 24.sp,
                 fontWeight = FontWeight.Bold,
                 textAlign = TextAlign.Center,
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 24.dp)
             )
         }
     }

 }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginPage(){
    val scope = rememberCoroutineScope()
    val scaffoldState = remember{SnackbarHostState()}
    var textFieldState by remember {
        mutableStateOf("")
    }
    Scaffold (modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = scaffoldState)
        }
    ) {
        Column(horizontalAlignment =  Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TextField(value = textFieldState,
                label = {
                    Text(text = "Enter Your Name")
                },
                onValueChange = {
                    textFieldState = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {scope.launch {
                        scaffoldState.showSnackbar("Hello $textFieldState")
                    }
                },
            ) {
                Text(text = "Submit")
            }
        }
    }
}

 @Composable
 fun ChangeState(){
     Column (modifier = Modifier
         .fillMaxSize()
     ){
         val color = remember {
             mutableStateOf(Color.Yellow)
         }

         ColourBox(
             Modifier
                 .weight(1f)
                 .fillMaxSize()
         ){
             color.value = it
         }

         Box (
             Modifier
                 .background(color.value)
                 .weight(1f)
                 .fillMaxSize()
         ){

         }
     }
 }

 @Composable
 fun ColourBox(
     modifier: Modifier = Modifier,
     updateColour : (Color) -> Unit
 ){
     Box (
         modifier
             .background(Color.Blue)
             .clickable {
                 updateColour(
                     Color(
                         Random.nextFloat(),
                         Random.nextFloat(),
                         Random.nextFloat(),
                         1f
                     )
                 )
             }
     )
 }


 @Composable
 fun TextStyling(){
     val FontFamily = FontFamily(
         Font(R.font.lora_bold, FontWeight.Bold),
         Font(R.font.lora_italic, FontWeight.Light),
         Font(R.font.lora_medium, FontWeight.Medium),
         Font(R.font.lora_regular, FontWeight.Normal),
         Font(R.font.lora_semibold, FontWeight.SemiBold),
     )
     Box(modifier = Modifier
         .fillMaxSize()
         .background(Color(0xFF101010))
     ){
         Text(
             text =  buildAnnotatedString {
                 withStyle(
                     style = SpanStyle(
                         color = Color.Green,
                         fontSize = 50.sp
                     )
                 ){
                     append("J")
                 }
                 append("etpack ")
                 withStyle(
                     style = SpanStyle(
                         color = Color.Green,
                         fontSize = 50.sp
                     )
                 ){
                     append("C")
                 }
                 append("ompose")
             },
             color = Color.White,
             fontSize = 30.sp,
             fontFamily = FontFamily,
             fontWeight = FontWeight.Bold,
             fontStyle = FontStyle.Italic,
             textAlign = TextAlign.Center,
             textDecoration = TextDecoration.Underline
         )
     }
 }

 @Composable
 fun ImageStyling(){
     Box(modifier = Modifier.padding(10.dp)){
         val painter = painterResource(id = R.drawable.mickeymouse)
         val description = "This is the Image of Nabh"
         val title = "Nabh Rajput is the owner of the this App"
         ImageCard(
             painter = painter,
             contentDescription = description,
             title = title
         )
     }
 }

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription : String,
    title : String,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier.fillMaxWidth(0.4f),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Box(modifier = Modifier.height(200.dp)){
            Image(painter = painter,
                contentScale = ContentScale.Fit,
                contentDescription = contentDescription
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomStart,
            ){
                Text(text =  title, style = TextStyle(color = Color.White,fontSize = 16.sp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ComposeCourseTheme {
        DraggableMusicKnob()
    }
}