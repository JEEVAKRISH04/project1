package com.example.firstproject

import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
       setContent {
            MyApp(preferencesManager)
        }
    }
}


@Composable
fun MyApp(preferencesManager: PreferencesManager) {
    val navController = rememberNavController()
    var employeeList by remember { mutableStateOf(preferencesManager.getEmployeeList()) }

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") {
            MainScreen(
                navController = navController,
                preferencesManager.getEmployeeList(),
                preferencesManager = preferencesManager)
        }
        composable("employee_form_screen") {

            EmployeeDetailsForm(
                navController = navController,
                existingEmployee = null
            )
        }

        composable("employee_form_screen/{employeeData}") { backStackEntry ->
            val employeeDataJson = backStackEntry.arguments?.getString("employeeData")
            val employee = employeeDataJson?.let {
                Gson().fromJson(it, Employee::class.java)
            }

            EmployeeDetailsForm(navController, employee)
        }

        composable("employee_detail_screen/{employeeIndex}") { backStackEntry ->
            val employeeIndex = backStackEntry.arguments?.getString("employeeIndex")?.toInt() ?:0
            EmployeeSwipeableDetailScreen(
                navController = navController,
                employees = preferencesManager.getEmployeeList(),
                initialPage = employeeIndex
            )
        }
    }
}
