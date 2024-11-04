package com.example.firstproject

import android.app.TimePickerDialog
import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailsForm(
    navController: NavHostController,
    existingEmployee: Employee? = null
)
{
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var name by remember { mutableStateOf(existingEmployee?.name ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(existingEmployee?.email ?: "") }
    var emailError by remember { mutableStateOf(false) }
    var number by remember { mutableStateOf(existingEmployee?.number ?: "") }
    var selectedGender by remember { mutableStateOf(existingEmployee?.gender ?: "Male") }
    var selectedDepartment by remember { mutableStateOf(existingEmployee?.department ?: "") }
    var selectedDate by remember { mutableStateOf(existingEmployee?.dateOfJoining ?: "Select Date") }
    var selectedTime by remember { mutableStateOf(existingEmployee?.shiftTime ?: "Select Time") }

    fun isValidName() = name.isNotBlank()
    val departmentOptions = listOf("Engineering", "Marketing", "HR", "Finance")




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Enter Your Detail")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFe8e9e7))
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Employee Name",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = !isValidName()
                                        },
                        isError = nameError,
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.weight(2f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Email",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = email,

                        onValueChange = { email = it
                            emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        isError = emailError,
                        label = { Text("Email") },
                        modifier = Modifier.weight(2f)
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Phone Number",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = number,
                        onValueChange = { Text ->
                            if (Text.all { it.isDigit() }) {
                                number = Text
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        label = { Text("Mobile Number") },
                        modifier = Modifier.weight(2f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gender",
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(2f)
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
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {

                var expanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Department", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(2f)) {
                        OutlinedTextField(
                            value = selectedDepartment,
                            onValueChange = { },
                            modifier = Modifier.clickable { expanded = true },
                            label = { Text("Department",modifier = Modifier.clickable { expanded = true }) },
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
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Date of Joining", modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.weight(1.5f),
                        onClick = {
                            showDatePicker(context,selectedDate) { date -> selectedDate = date }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedDate != "Select Date") Color(
                                0xFF5C95EC
                            ) else Color(0xFFaac2e7)
                        )
                    ) {
                        Text(text = selectedDate, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Shift Time", modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.weight(1.5f),
                        onClick = {
                            showTimePicker(context,selectedTime) { time -> selectedTime = time }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTime != "Select Time") Color(
                                0xFF5C95EC
                            ) else Color(0xFFaac2e7)
                        )
                    ) {
                        Text(text = selectedTime, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {

                            if (!emailError) {
                                val updatedEmployee = Employee(
                                    name,
                                    email,
                                    number,
                                    selectedGender,
                                    selectedDepartment,
                                    selectedDate,
                                    selectedTime
                                )
                                if (existingEmployee != null) {
                                    preferencesManager.updateEmployee( existingEmployee, updatedEmployee)
                                } else {  preferencesManager.saveEmployee(updatedEmployee) }

                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97E99B))
                    ) {
                        Text(text = "Submit", color = Color.White)
                    }
                }
            }
        }
    }
}

fun showDatePicker(context: Context, selectedDate: String, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()


    if (selectedDate != "Select Date") {
        val parts = selectedDate.split("-")
        calendar.set(Calendar.DAY_OF_MONTH, parts[0].toInt())
        calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
        calendar.set(Calendar.YEAR, parts[2].toInt())
    }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(String.format("%02d-%02d-%02d", dayOfMonth, month + 1, year))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

fun showTimePicker(context: Context, selectedTime: String, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    if(selectedTime != "Select Time") {
        val parts = selectedTime.split(":")
        val hour =parts[0].toInt()
        val minute =parts[1].split(" ")[0].toInt()
        val isPM =selectedTime.contains("PM")
        calendar.set(Calendar.HOUR_OF_DAY,if (isPM && hour != 12) hour + 12 else if (!isPM && hour == 12) 0 else hour)
        calendar.set(Calendar.MINUTE,minute)
    }
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val isPM = hourOfDay >= 12
            onTimeSelected(String.format("%02d:%02d %s", if (hourOfDay % 12 == 0) 12 else  hourOfDay % 12, minute, if (isPM) "PM" else "AM"))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )
    timePickerDialog.show()
}

