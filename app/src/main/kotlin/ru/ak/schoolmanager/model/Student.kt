package ru.ak.schoolmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ak.schoolmanager.db.DateConverter
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "students")
@TypeConverters(DateConverter::class)
data class Student(
    @ColumnInfo(name = "fio")
    var fio: String,

    @ColumnInfo(name = "birthDate")
    var birthDate: LocalDate?,

    @ColumnInfo(name = "resp1")
    var resp1: Int,

    @ColumnInfo(name = "respFio1")
    var respFio1: String,

    @ColumnInfo(name = "respPhone1")
    var respPhone1: String,

    @ColumnInfo(name = "resp2")
    var resp2: Int,

    @ColumnInfo(name = "respFio2")
    var respFio2: String,

    @ColumnInfo(name = "respPhone2")
    var respPhone2: String,

    @ColumnInfo(name = "note")
    var note: String,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
