@file:OptIn(ExperimentalPagerApi::class)

package com.example.firstproject
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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



@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmployeeSwipeableDetailScreen(navController: NavHostController, employees: List<Employee>, initialPage: Int) {
    val pagerState = rememberPagerState(initialPage = initialPage)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().background(Color(0XFFF8B8E4))

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = if (pagerState.currentPage > 0) employees[pagerState.currentPage - 1].name else "",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (pagerState.currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                maxLines = 1,
                textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFf51bb1))

            )


            Text(
                text = employees[pagerState.currentPage].name,
                modifier = Modifier
                    .weight(1f),
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFf51bb1)) // Accent color

            )


            Text(
                text = if (pagerState.currentPage < employees.size - 1) employees[pagerState.currentPage + 1].name else "",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (pagerState.currentPage < employees.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                maxLines = 1,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFf51bb1)) // Accent color

            )
        }


        HorizontalPager(
            state = pagerState,
            count = employees.size,
            modifier = Modifier.weight(1f)
        ) { page ->
            EmployeeDetailTab(employee = employees[page])
        }
    }
}




@Composable
fun EmployeeDetailTab(employee: Employee) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Employee Name: ${employee.name}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFFf51bb1),
                    fontWeight = FontWeight.Bold
                )
            )
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
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



