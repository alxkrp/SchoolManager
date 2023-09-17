package ru.ak.schoolmanager

import android.content.Context
import androidx.room.Room
import ru.ak.schoolmanager.db.SchoolRoomDatabase
import ru.ak.schoolmanager.db.repository.StudentsRepository

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    val appDatabase: SchoolRoomDatabase by lazy {
        Room.databaseBuilder(applicationContext, SchoolRoomDatabase::class.java, "school.db")
//            .createFromAsset("room_article.db")
            .build()
    }

    val statisticRepository: StudentsRepository by lazy { StudentsRepository(appDatabase.getStudentDao()) }
}