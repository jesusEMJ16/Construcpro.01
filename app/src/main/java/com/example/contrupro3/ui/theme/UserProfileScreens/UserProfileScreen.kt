package com.example.contrupro3.ui.theme.UserProfileScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.mywhie


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileView(navController: NavController, authRepository: AuthRepository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mywhie),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        userimagen()
        Spacer(modifier = Modifier.height(15.dp))
        RowWithDivider()
        Spacer(modifier = Modifier.height(5.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        sectionMedium()
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SectionDown()
    }
    HamburgueerMenu(navController = navController, authRepository = authRepository)

}

//FALSO ABAJO
@Composable
fun UserProfileView2() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = mywhie),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        userimagen()
        Spacer(modifier = Modifier.height(15.dp))
        RowWithDivider()
        Spacer(modifier = Modifier.height(5.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        sectionMedium()
        Spacer(modifier = Modifier.height(15.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SectionDown()
    }
}

//FALSO ARRIBA


@Composable
fun RowWithDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = (-10).dp)
            .background(Color.Transparent)
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        numberprojects()
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(65.dp)
                .width(1.dp)
        )
        username()
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(65.dp)
                .width(1.dp)
        )
        userprofits()
    }
}

@Composable
fun userimagen() {
    Box(
        modifier = Modifier
            .size(135.dp) // Tamaño total del Box, incluyendo el marco
            .background(color = Color.LightGray, shape = CircleShape) // Color del marco
            .padding(3.dp), // Espesor del marco
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(132.dp) // Tamaño de la imagen, que es menor al del Box para mantener el efecto de marco
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun username() {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Jesus",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Text(
            text = "Moncada",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }

}

@Composable
fun ProjectIcon() {
    Icon(
        painter = painterResource(id = R.drawable.project2),
        contentDescription = "Icono de proyectos",
        Modifier.size(35.dp)
    )
}

@Composable
fun ProfitIcon() {
    Icon(
        painter = painterResource(id = R.drawable.profit),
        contentDescription = "Icono de proyectos",
        Modifier.size(35.dp)
    )
}

@Composable
fun numberprojects() {
    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        ProjectIcon()
        Text(
            text = "Projects",
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
        )
        Text(
            text = "5",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun userprofits() {
    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        ProfitIcon()
        Text(
            text = "Profit",
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
        )
        Text(
            text = "2220",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sectionMedium() {
    var name by remember { mutableStateOf("Jesus") }
    var lastName by remember { mutableStateOf("Moncada") }
    var email by remember { mutableStateOf("jesusEMJ16@gmail.com") }


    //Name
    Column {
        Text(
            text = "Name",
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { newName ->
                name = newName
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = myBlue,
                disabledBorderColor = Color.Transparent,
                containerColor = Color.LightGray
                ),
            textStyle = TextStyle(fontWeight = FontWeight.Medium),
        )
    }

    //Last name
    Column {
        Text(
            text = "Last name",
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { newLastName ->
                lastName = newLastName
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = myBlue,
                disabledBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            ),
            textStyle = TextStyle(fontWeight = FontWeight.Medium),
        )
    }

    //Last name
    Column {
        Text(
            text = "Email",
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { newEmail ->
                email = newEmail
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = myBlue,
                disabledBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            ),
            textStyle = TextStyle(fontWeight = FontWeight.Medium),
        )
    }
}

// Datos de la seccion de abajo
data class Project(val name: String, val profit: Double)

// Lista prefijada de proyectos
val sampleProjects = listOf(
    Project("Project Alpha", 555.99),
    Project("Project Betasdfsdfsdf", 10323.56),
    Project("Project Bet dsf ds dsfdsfsffa", 461782.55),
    Project("Project Beta", 91919383.44),
    Project("Project Beta largo texto largo", 12823813.33)
)

// Composable para mostrar un ítem de proyecto (Es el que muestra la vista debajo de "Projects >"
@Composable
fun ProjectItem(project: Project) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, top = 4.dp, end = 16.dp)
    ) {
        Text(
            text = project.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = project.profit.toString(),
            fontSize = 14.sp
        )
    }
}

@Composable
fun SectionDown() {
    var isExpanded by remember { mutableStateOf(false) } // Estado para rastrear la expansión

    Column {
        Header(isExpanded) { isExpanded = !isExpanded } // Pasar el estado y la función de cambio como argumentos

        AnimatedVisibility(visible = isExpanded) { // Mostrar la lista si isExpanded es true
            ProjectList()
        }
    }
}

@Composable
fun Header(isExpanded: Boolean, onHeaderClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onHeaderClick() }, // Hacer la fila clickeable
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Projects",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
            contentDescription = "Toggle Arrow",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.weight(0.8f))
        Icon(
            imageVector = Icons.Default.AttachMoney,
            contentDescription = "Money",
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun ProjectList() {
    LazyColumn {
        items(sampleProjects) { project ->
            ProjectItem(project)
        }
    }
}


@Composable
@Preview
fun preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mywhie),
    ) {
        UserProfileView2()
    }
}
