@file:OptIn(ExperimentalPagerApi::class)

package com.example.firstproject

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmployeeSwipeableDetailScreen(
    navController: NavHostController,
    employees: List<Employee>,
    initialPage: Int,
    preferencesManager: PreferencesManager) {

    var employeeList by remember { mutableStateOf(preferencesManager.getEmployeeList())  }

    val pagerState = rememberPagerState(initialPage = initialPage.coerceAtMost(employeeList.size - 1))
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFFe8e9e7)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val prevEmployee = if (pagerState.currentPage > 0) employeeList.getOrNull(pagerState.currentPage - 1)?.name.orEmpty() else ""
            if (prevEmployee.isEmpty()) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            } else {
                Box(
                    modifier = Modifier.height(70.dp)
                        .weight(1f).background(Color(0xFFe8e9e7),
                            shape = RoundedCornerShape(
                            bottomEnd =  26.dp
                        ))
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color(0xFF4c4c4c),

                        text = prevEmployee.ifEmpty { " " },
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                       )
                }
            }


            Box(
                modifier = Modifier.height(70.dp).padding(2.dp)
                    .weight(1.5f).background(Color(0xFFd0d1cf),
                        shape = RoundedCornerShape( 16.dp
                        )),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text =  employeeList.getOrNull(pagerState.currentPage)?.name.orEmpty(),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF000000),

                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.5f
                    )
                )
            }


            val nextEmployee = if (pagerState.currentPage < employees.size - 1)  employeeList.getOrNull(pagerState.currentPage + 1)?.name.orEmpty() else ""
            if (nextEmployee.isEmpty()) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate("employee_form_screen")
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            } else {
                Box(
                    modifier = Modifier.height(70.dp)
                        .weight(1f).background(Color(0xFFe8e9e7),
                            shape = RoundedCornerShape(
                                bottomStart =  26.dp
                            ))
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color(0xFF4c4c4c),

                        text = nextEmployee,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }



        if (employeeList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE7ECF3)),
                contentAlignment = Alignment.Center
            ) {

            HorizontalPager(
                state = pagerState,
                count = employeeList.size,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                EmployeeDetailTab(
                    employee = employeeList[page],
                    onEditClick = { index ->
                        val employeeData = Gson().toJson(employeeList[index])
                        navController.navigate("employee_form_screen/$employeeData")
                    },
                    onDeleteClick = { index ->
                        preferencesManager.deleteEmployeeAtIndex(index)
                        employeeList = preferencesManager.getEmployeeList()
                        coroutineScope.launch {
                            if(employeeList.isNotEmpty()) {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage.coerceAtMost(employeeList.size - 1)
                                )
                            }else{
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    },
                    index = page
                )
            }
        }
        }else {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }
}
@Composable
fun EmployeeDetailTab(
    employee: Employee,
    onDeleteClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFe6edf8)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    maxLines = 1,
                    text = "Employee Name: ${employee.name}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF000000),
                        fontWeight = FontWeight.Bold
                    )
                )
                Divider(color = Color(0xFF12A3E7), thickness = 1.dp)
                Text(
                    text = "Email: ${employee.email}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Phone Number: ${employee.number}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Gender: ${employee.gender}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Department: ${employee.department}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Date of Joining: ${employee.dateOfJoining}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Shift Time: ${employee.shiftTime}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { onEditClick(index) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF82E2EE)),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Edit", color = Color.White)
                }

                Button(
                    onClick = { onDeleteClick(index) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA709F)),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Delete", color = Color.White)
                }
            }
        }
    }
}
