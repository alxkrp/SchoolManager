package ru.ak.schoolmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ak.schoolmanager.databinding.ActivityAddStudentBinding
import ru.ak.schoolmanager.model.Student
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            student = intent.getSerializableExtra("Data") as Student
        } catch (e: Exception) {
            Log.e("", e.toString())
        }

        if (student != null) {
            binding.edFio.setText(student?.fio)
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            if (student?.birthDate != null) {
                binding.edBirthDate.setText(student?.birthDate!!.format(formatter))
            }
            binding.spResp1.setSelection(student?.resp1!!)
            binding.edRespFio1.setText(student?.respFio1)
            binding.edRespPhone1.setText(student?.respPhone1)
            binding.spResp2.setSelection(student?.resp2!!)
            binding.edRespFio2.setText(student?.respFio2)
            binding.edRespPhone2.setText(student?.respPhone2)
            binding.edNote.setText(student?.note)
        }

        binding.btnAddOrUpdateStudent.setOnClickListener { addOrUpdateStudent() }
    }

    private fun addOrUpdateStudent() {
        val fio = binding.edFio.text.toString()
        var birthDate: LocalDate? = null
        val birthDateStr = binding.edBirthDate.text.toString()
        if (birthDateStr.isNotBlank()) {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            birthDate = LocalDate.parse(birthDateStr, formatter)
        }
        val resp1 = binding.spResp1.selectedItemPosition
        val respFio1 = binding.edRespFio1.text.toString()
        val respPhone1 = binding.edRespPhone1.text.toString()
        val resp2 = binding.spResp2.selectedItemPosition
        val respFio2 = binding.edRespFio2.text.toString()
        val respPhone2 = binding.edRespPhone2.text.toString()
        val note = binding.edNote.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val s = Student(fio, birthDate, resp1, respFio1, respPhone1, resp2, respFio2, respPhone2, note)
            if (student == null) {
                Dependencies.appDatabase.getStudentDao().addStudent(s)
                if (s.birthDate != null) {
                    addAlarm(s)
                }
            } else {
                s.id = student?.id ?: 0
                Dependencies.appDatabase.getStudentDao().updateStudent(s)
                if (s.birthDate != null && !s.birthDate!!.equals(student!!.birthDate)) {
                    addAlarm(s)
                }
            }
            finish()
        }
    }

    private fun addAlarm(student: Student) {
//        val alarm = AlarmBroadcast()
////        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        val alarmTime = LocalDateTime.of(student.birthDate?.withYear(LocalDate.now().year),
//                LocalTime.now().plusMinutes(1))
////        val alarmTimeStr = alarmTime.withYear(2023).format(formatter)
////        val alarmTime = LocalDateTime.now().plusMinutes(1).format(formatter)
//        alarm.setAlarm(applicationContext, alarmTime)
////        alarm.alert(applicationContext)
    }
}