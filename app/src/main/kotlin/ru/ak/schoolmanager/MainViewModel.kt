package ru.ak.schoolmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ak.schoolmanager.db.repository.StudentsRepository
import ru.ak.schoolmanager.model.Student

class MainViewModel(private val studentsRepository: StudentsRepository): ViewModel() {
    fun addStudent(student: Student) {
        viewModelScope.launch {
            studentsRepository.addStudent(student)
        }
    }

    fun deleteStudent(id: Int) {
        viewModelScope.launch {
            studentsRepository.deleteStudent(id)
        }
    }

    suspend fun getStudentsAll(): List<Student> {
        val students = CoroutineScope(Dispatchers.IO).async {
            return@async studentsRepository.getStudentsAll()
        }
        return students.await()
    }
}