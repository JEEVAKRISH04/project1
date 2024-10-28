import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firstproject.Employee
import com.example.firstproject.PreferencesManager
import com.google.gson.Gson

@Composable
fun MainScreen(navController: NavHostController,
               employeeList: List<Employee>,
               preferencesManager: PreferencesManager) {

    var employeeList by remember { mutableStateOf(preferencesManager.getEmployeeList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        employeeList = preferencesManager.getEmployeeList()
    }


    Scaffold(
        topBar = {
            UniqueTopAppBar(navController, isSearching, onSearchIconClick = {
                isSearching = !isSearching
                searchText = ""
            }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("employee_form_screen")
                          },
                contentColor = Color.White,
                containerColor = Color(0xFFaac2e7)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add ")
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFe6edf8))
                .padding(paddingValues)
        )


        {

            Column {
                if (isSearching) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { query ->
                            searchText = query
                            employeeList = if (query.isNotEmpty()) {
                                val matchingEmployee = preferencesManager.getEmployeeList()
                                    .find { it.name.contains(query, ignoreCase = true) }
                                if (matchingEmployee != null) {
                                    listOf(matchingEmployee) + preferencesManager.getEmployeeList()
                                        .filter { it != matchingEmployee }
                                } else {
                                    preferencesManager.getEmployeeList()
                                }
                            } else {
                                preferencesManager.getEmployeeList()
                            }
                        },
                        label = { Text("Search Employee") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true
                    )
                }



                LazyScrollableCardList(
                    employeeList,
                    onCardClick = { index ->
                        navController.navigate("employee_detail_screen/$index")
                    }, onDeleteClick = { index ->
                        preferencesManager.deleteEmployeeAtIndex(index)
                        employeeList = preferencesManager.getEmployeeList()

                    },
                    onEditClick = { index ->
                        val employeeData = Gson().toJson(employeeList[index])
                        navController.navigate("employee_form_screen/$employeeData")
                    }

                )
            }

        }
    }
}


@Composable
fun UniqueTopAppBar(navController: NavHostController, isSearching: Boolean, onSearchIconClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFFe8e9e7))
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
                contentDescription = "Menu"
            )

            Text(
                text = "Employee List",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Add Employee",
                modifier = Modifier.clickable { onSearchIconClick() }
            )
        }
    }
}


@Composable
fun LazyScrollableCardList(
    employeeList: List<Employee>,
    onCardClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(employeeList.size) { index ->
            CardItem(
                employee = employeeList[index],
                onCardClick = { onCardClick(index) },
                onDeleteClick = { onDeleteClick(index) },
                onEditClick = { onEditClick(index) }
            )
        }
    }
}



@Composable
fun CardItem(employee: Employee,
             onCardClick: () -> Unit,
             onDeleteClick: () -> Unit,
             onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(5.dp).padding(10.dp,0.dp,10.dp,0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Name: ${employee.name}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Department: ${employee.department}"
                    )
                }
                Column {

                    Button(
                        onClick = { onEditClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF82E2EE)),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(text = "Edit", color = Color.White)
                    }


                    Button(
                        onClick = { onDeleteClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA709F)),
                        modifier = Modifier.width(100.dp)

                    ) {
                        Text(text = "Delete", color = Color.White)
                    }
                }
            }
        }
    }
}

