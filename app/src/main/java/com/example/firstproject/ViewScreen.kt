@file:OptIn(ExperimentalPagerApi::class)

package com.example.firstproject

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmployeeSwipeableDetailScreen(navController: NavHostController, employees: List<Employee>, initialPage: Int) {
    val pagerState = rememberPagerState(initialPage = initialPage)
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

            val prevEmployee = if (pagerState.currentPage > 0) employees[pagerState.currentPage - 1].name else ""
            if (prevEmployee.isEmpty()) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Back")
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
                        shape = RoundedCornerShape(
                            topStart =  16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employees[pagerState.currentPage].name,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF000000),

                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.5f
                    )
                )
            }


            val nextEmployee = if (pagerState.currentPage < employees.size - 1) employees[pagerState.currentPage + 1].name else ""
            if (nextEmployee.isEmpty()) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Back")
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



        Box(modifier = Modifier
            .weight(1f)
            .background(Color(0xFFE7ECF3))) {
            HorizontalPager(
                state = pagerState,
                count = employees.size,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                EmployeeDetailTab(employee = employees[page])
            }
        }
    }
}

@Composable
fun EmployeeDetailTab(employee: Employee) {
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        )
        {
            Text(
                maxLines = 1,
                text = "Employee Name: ${employee.name}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Bold
                )

            )
            Divider(color =Color(0xFF12A3E7), thickness = 1.dp)
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
    }
}
