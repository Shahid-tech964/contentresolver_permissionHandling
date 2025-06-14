package com.example.demo_access

import android.Manifest.permission.READ_CALL_LOG
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.demo_access.ui.theme.Demo_accessTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
    val viewmodel:customviewmodel by viewModels()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Demo_accessTheme {
      CallLogScreen(viewmodel)
            }
        }
    }


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallLogScreen(viewmodel: customviewmodel) {
   val lifecycleOwner= LocalLifecycleOwner.current
    val permissionStatus= rememberPermissionState(permission =android.Manifest.permission.READ_CALL_LOG )

     DisposableEffect(lifecycleOwner) {
         val observer= LifecycleEventObserver{source, event ->
             if(event==Lifecycle.Event.ON_START){
                 permissionStatus.launchPermissionRequest()
             }
         }
         lifecycleOwner.lifecycle.addObserver(observer)

         onDispose {
             lifecycleOwner.lifecycle.removeObserver(observer)
         }
     }

    when{
        permissionStatus.status.isGranted->{

           DisplayUser(viewmodel)
        }

        permissionStatus.status.shouldShowRationale->{
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("❗ This permission is needed to access your call logs.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        permissionStatus.launchPermissionRequest()
                    }) {
                        Text("Request Again")
                    }
                }

        }

        else->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("❌ Permission denied or not yet requested.")
            }

        }
    }

   




}




@Composable
fun  DisplayUser(viewmodel:customviewmodel) {
  val scope= rememberCoroutineScope()
    val context= LocalContext.current
    val state by viewmodel.liveData.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewmodel.fetch_result(context)
    }

    when(state){
        is sealedclass.error -> {
            val error=(state as sealedclass.error).error
             Box(modifier = Modifier.fillMaxSize()) {
       Text(text = error, color= Color.Black,fontSize = 20.sp, modifier = Modifier.align(Alignment.Center))
                 
             }
        }
        sealedclass.loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = Color.Green, modifier = Modifier.align(Alignment.Center))

            }
        }
        is sealedclass.suceess -> {
            val data =(state as sealedclass.suceess).mess

            var name by  remember {
                mutableStateOf("")
            }

            var course  by  remember {
                mutableStateOf("")
            }


            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)) {
//        for TextField
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)) {

                    TextField(value = name, onValueChange ={it->
                        name=it
                    } , modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                        placeholder = {
                            Text(text = "Enter Name",fontSize = 15.sp, color = Color.White)
                        },

                        colors = TextFieldDefaults.colors(focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Gray,
                            unfocusedContainerColor = Color.Gray,
                            cursorColor = Color.Green,
                            disabledIndicatorColor = Color.Unspecified)

                        , singleLine = true,

                        shape = RoundedCornerShape(5.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    TextField(value = course, onValueChange ={it->
                        course=it
                    } , modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                        placeholder = {
                            Text(text = "Enter Course",fontSize = 15.sp, color = Color.White)
                        },

                        colors = TextFieldDefaults.colors(focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Gray,
                            unfocusedContainerColor = Color.Gray,
                            cursorColor = Color.Green,
                            disabledIndicatorColor = Color.Unspecified)

                        , singleLine = true,
                        shape = RoundedCornerShape(8.dp)

                    )

                    Spacer(modifier = Modifier.height(6.dp))


//       button  for adding users in database

                    Button(onClick = {
                        scope.launch {
                            viewmodel.addUser(name, course, context)
                        }
                    }, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(40.dp)
                        .width(150.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) ) {
                        Text(text = "Add User", fontSize = 17.sp, color = Color.White)
                    }

                }





                Spacer(modifier = Modifier.height(20.dp))

//         for displaying data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(data){it->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(color = Color.DarkGray)){
                            Text(text = it.name, fontSize = 15.sp, modifier = Modifier.align(Alignment.TopStart), color = Color.White)
                            Text(text = it.course, fontSize = 15.sp, modifier = Modifier.align(Alignment.BottomStart), color = Color.White)

                            IconButton(onClick = {
                                scope.launch {
                                    viewmodel.deleteUser(context, it.id)
                                }
                            }, modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(30.dp)) {
                                Icon(imageVector =Icons.Default.Delete, contentDescription = null, tint = Color.White )
                            }




                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
        null -> {

        }
    }




}



