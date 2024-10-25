package com.example.firstproject

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    var selectedDepartment by remember { mutableStateOf("Engineering") }

    val departmentOptions = listOf("Engineering", "Marketing", "HR", "Finance")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Employee Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFFF8B8E4),
                unfocusedLabelColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Gender", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedDepartment,
                onValueChange = { },
                label = { Text("Department") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                departmentOptions.forEach { department ->
                    DropdownMenuItem(
                        text = { Text(department) },
                        onClick = {
                            selectedDepartment = department
                            expanded = false // Close dropdown after selection
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { showDatePicker(context) { date -> selectedDate = date } },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf51bb1))
        ) {
            Text(text = selectedDate, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { showTimePicker(context) { time -> selectedTime = time } },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf51bb1))
        ) {
            Text(text = selectedTime, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf51bb1))
        ) {
            Text(text = "Submit", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf51bb1))
        ) {
            Text(text = "Back", color = Color.White)
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



