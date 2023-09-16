package ru.ak.schoolmanager.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ak.schoolmanager.db.dao.StudentDao
import ru.ak.schoolmanager.model.Student

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ /**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.  In a real
 * app, consider exporting the schema to help you with migrations.
 */
@Database(
    entities = [Student::class],
    version = 1,
    autoMigrations = [
//       AutoMigration (from = 1, to = 2),
    ],
    exportSchema = true)
abstract class SchoolRoomDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: SchoolRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SchoolRoomDatabase::class.java,
            "school.db"
        ).build()
//        private const val NUMBER_OF_THREADS = 4
//        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
//        fun getDatabase(context: Context): SchoolRoomDatabase? {
//            if (INSTANCE == null) {
//                synchronized(SchoolRoomDatabase::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = databaseBuilder(
//                            context.applicationContext,
//                            SchoolRoomDatabase::class.java, "school.db"
//                        )
////                            .addCallback(sRoomDatabaseCallback)
//                            .build()
//                    }
//                }
//            }
//            return INSTANCE!!
//        }

//        fun getDatabase(context: Context): SchoolRoomDatabase {
//            return databaseBuilder(
//                context.applicationContext,
//                SchoolRoomDatabase::class.java, "school.db"
//            ).build()
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    /*    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);

        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            // If you want to start with more words, just add them.
            WordDao dao = INSTANCE.wordDao();
//                dao.deleteAll();

            Word word = new Word("Hello");
            dao.insert(word);
            word = new Word("World");
            dao.insert(word);
        });
    }
};*/
}