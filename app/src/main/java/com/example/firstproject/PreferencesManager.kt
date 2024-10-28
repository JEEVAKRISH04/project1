package com.example.firstproject

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson


class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("", Context.MODE_PRIVATE)


    private val EMPLOYEE_LIST_KEY = "111"


    fun saveEmployee(employee: Employee) {
        val currentList = getEmployeeList().toMutableList()
        currentList.add(employee)
        val json = Gson().toJson(currentList)
        sharedPreferences.edit().putString(EMPLOYEE_LIST_KEY, json).apply()
    }


    fun getEmployeeList(): List<Employee> {
        val json = sharedPreferences.getString(EMPLOYEE_LIST_KEY, "")
        val type = object : com.google.gson.reflect.TypeToken<List<Employee>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }


    fun deleteEmployeeAtIndex(index: Int) {
        val currentList = getEmployeeList().toMutableList()
        currentList.removeAt(index)
        val json = Gson().toJson(currentList)
        sharedPreferences.edit().putString(EMPLOYEE_LIST_KEY, json).apply()

    }


}




