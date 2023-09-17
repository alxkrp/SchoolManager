package ru.ak.schoolmanager.db.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ak.schoolmanager.db.dao.StudentDao
import ru.ak.schoolmanager.model.Student

class StudentsRepository(private val studentDao: StudentDao) {
    suspend fun addStudent(student: Student) {
        withContext(Dispatchers.IO) {
            studentDao.addStudent(student)
        }
    }

    suspend fun getStudentsAll(): List<Student> {
        return withContext(Dispatchers.IO) {
            return@withContext studentDao.getStudentsAll()
        }
    }

    suspend fun deleteStudent(id: Int) {
        withContext(Dispatchers.IO) {
            studentDao.deleteStudent(id)
        }

    }
}