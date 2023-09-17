package ru.ak.schoolmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ak.schoolmanager.db.dao.StudentDao
import ru.ak.schoolmanager.model.Student

@Database(
    entities = [Student::class],
    version = 1,
    autoMigrations = [
//       AutoMigration (from = 1, to = 2),
    ],
    exportSchema = true)
abstract class SchoolRoomDatabase : RoomDatabase() {
    abstract fun getStudentDao(): StudentDao
}