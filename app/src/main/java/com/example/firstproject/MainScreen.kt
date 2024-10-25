package com.example.firstproject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController




@Composable
fun MainScreen(navController: NavHostController, employeeList: List<Employee>) {
    Scaffold(
        topBar = {
            UniqueTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("employee_form_screen") },
                contentColor = Color.White,
                containerColor = Color(0xFFf51bb1)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8B8E4))
            .padding(paddingValues)
        ) {
            LazyScrollableCardList(employeeList) { index ->
                navController.navigate("employee_detail_screen/$index")
            }
        }
    }
}

@Composable
fun UniqueTopAppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFFf51bb1), Color(0xFFf8b8e4))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = Color.White
            )


            Text(
                text = "Employee List",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )


            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Employee",
                tint = Color.White
            )
        }
    }
}

@Composable
fun LazyScrollableCardList(
    employeeList: List<Employee>,
    onCardClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(employeeList.size) { index ->
            CardItem(employee = employeeList[index], onCardClick = { onCardClick(index) })
        }
    }
}

@Composable
fun CardItem(employee: Employee, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Name: ${employee.name}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFf51bb1))
            )
            Text(
                text = "Department: ${employee.department}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}
