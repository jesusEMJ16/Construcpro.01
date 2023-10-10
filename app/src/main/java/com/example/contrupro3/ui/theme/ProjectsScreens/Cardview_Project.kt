package com.example.contrupro3.ui.theme.ProjectsScreens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Project
import com.example.contrupro3.ui.theme.myOrange
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.google.firebase.firestore.FirebaseFirestore


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardviewProjectsScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    projectId: String
) {
    val projectsList = remember { mutableStateOf<Project?>(null) }
    authRepository.loadProject(projectId, projectsList)
    val project = projectsList.value
    val viewModel: CardViewProjectViewModel = viewModel()
    val projectName by viewModel.projectName
    val projectDescription by viewModel.projectDescription
    viewModel.projectName.value = project?.projectName.toString()
    viewModel.projectDescription.value = project?.description.toString()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(projectName, style = MaterialTheme.typography.titleLarge)
                        Text(project?.creatorName ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = myOrange)
                    }
                },
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset(y = 50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))
                InformationCard(navController, authRepository, userID, project)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
private fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    project: Project?
) {
    val viewModel: CardViewProjectViewModel = viewModel()
    val nameEnabled by viewModel.nameEnabled
    val descriptionEnabled by viewModel.descriptionEnabled
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val projectName by viewModel.projectName
    val projectDescription by viewModel.projectDescription
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.material3.Card(
            colors = CardDefaults.cardColors(
                containerColor = myOrangelow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Información del proyecto",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = myOrange
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nombre del proyecto",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    maxLines = 1,
                    placeholder = { Text(text = "${projectName}") },
                    singleLine = true,
                    enabled = nameEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (nameEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.nameEnabled.value = !viewModel.nameEnabled.value
                            viewModel.descriptionEnabled.value = false
                        }) {
                            if (nameEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar nombre del proyecto"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar nombre del proyecto"
                            )
                        }
                    }
                )
                if(nameEnabled) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if(name.length < 6) {
                            Text(
                                text = "* Requerido",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = myOrange),
                                modifier = Modifier.offset(x = 5.dp)
                            )
                        } else Spacer(modifier = Modifier.width(0.dp))
                        Text(
                            text = "${name.length}/30",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(name.length < 6 || name.length > 30) myOrange else Color.Black)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Descripción del proyecto",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    maxLines = 1,
                    placeholder = {
                        if(projectDescription != null && projectDescription.length > 0) {
                            Text("${projectDescription}")
                        } else {
                            Text("No hay descripción establecida.")
                        }
                    },
                    singleLine = true,
                    enabled = descriptionEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (descriptionEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.descriptionEnabled.value = !viewModel.descriptionEnabled.value
                            viewModel.nameEnabled.value = false
                        }) {
                            if (descriptionEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar nombre del proyecto"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar nombre del proyecto"
                            )
                        }
                    }
                )
                if(descriptionEnabled) {
                    Text(
                        text = "${description.length}/200",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(description.length > 200) myOrange else Color.Black),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
    if(name.length > 0 || description.length > 0) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    name = ""
                    description = ""
                    viewModel.nameEnabled.value = false
                    viewModel.descriptionEnabled.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Cancelar")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    val db = FirebaseFirestore.getInstance()
                    val collection = db.collection("Usuarios").document(userID).collection("Proyectos").document(project?.id.toString())

                    if(name.length > 0) {
                        viewModel.projectName.value = name
                        collection.update("projectName", "$name")
                    }
                    if(description.length > 0) {
                        viewModel.projectDescription.value = description
                        collection.update("description", "$description")
                    }
                    viewModel.nameEnabled.value = false
                    viewModel.descriptionEnabled.value = false
                    name = ""
                    description = ""
                    Toast.makeText(
                        context,
                        "Datos Actualizados",
                        Toast.LENGTH_LONG
                    ).show()
                },
                enabled = name.length >= 6 && name.length <= 30 || description.length > 6 && description.length <= 200,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Guardar")
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

class CardViewProjectViewModel: ViewModel() {
    val nameEnabled = mutableStateOf(false)
    val descriptionEnabled = mutableStateOf(false)
    val projectName = mutableStateOf("")
    val projectDescription = mutableStateOf("")
}