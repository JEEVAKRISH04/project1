package com.example.firstproject

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailsForm(navController: NavHostController, newEmployee: (Employee) -> Unit) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var selectedDate by remember { mutableStateOf("Select Date") }
    var selectedTime by remember { mutableStateOf("Select Time") }
    var name by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var selectedDepartment by remember { mutableStateOf("") }

    val departmentOptions = listOf("Engineering", "Marketing", "HR", "Finance")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Enter Your Detail",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFe8e9e7))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Employee Name")
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.weight(.5f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Gender")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedGender == "Male",
                        onClick = { selectedGender = "Male" }
                    )
                    Text(text = "Male")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedGender == "Female",
                        onClick = { selectedGender = "Female" }
                    )
                    Text(text = "Female")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Department")
                Spacer(modifier = Modifier.width(40.dp))
                Box(modifier = Modifier.weight(.5f)) {
                    OutlinedTextField(
                        value = selectedDepartment,
                        onValueChange = { },
                        label = { Text("Department") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                modifier = Modifier.clickable { expanded = true },
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        departmentOptions.forEach { department ->
                            DropdownMenuItem(
                                text = { Text(department) },
                                onClick = {
                                    selectedDepartment = department
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            var submittedDate by remember { mutableStateOf("") }
            var submittedTime by remember { mutableStateOf("") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Date of Joining")
                Button(
                    onClick = { showDatePicker(context) { date -> selectedDate = date }
                        submittedDate = selectedDate },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedDate != "Select Date") Color(0xFF5C95EC) else Color(0xFFaac2e7) ) ){
                    Text(text = selectedDate, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Shift Time")
                Button(
                    onClick = { showTimePicker(context) { time -> selectedTime = time }
                        submittedTime = selectedTime},
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTime != "Select Time") Color(0xFF5C95EC) else Color(0xFFaac2e7) ) ){

                Text(text =selectedTime, color = Color.White)

                    }
            }
            Spacer(modifier = Modifier.height(32.dp))



            Button(
                onClick = {
                    val newEmployee = Employee(
                        name = name,
                        gender = selectedGender,
                        department = selectedDepartment,
                        dateOfJoining = selectedDate,
                        shiftTime = selectedTime
                    )
                    preferencesManager.saveEmployee(newEmployee)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1491F5))
            ) {
                Text(text = "Submit", color = Color.White)
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth -> onDateSelected("$dayOfMonth/${month + 1}/$year") },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute -> onTimeSelected(String.format("%02d:%02d", hourOfDay, minute)) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )
    timePickerDialog.show()
}
