package ru.ak.schoolmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ak.schoolmanager.model.Student

@Dao
interface StudentDao {
    @Query("select * from students order by fio")
    fun getStudentsAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStudent(student: Student)

    @Update
    fun updateStudent(student: Student)

    @Query("delete from students where id = :id")
    fun deleteStudent(id: Int)
}