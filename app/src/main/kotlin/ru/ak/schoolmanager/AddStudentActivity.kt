package ru.ak.schoolmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ak.schoolmanager.databinding.ActivityAddStudentBinding
import ru.ak.schoolmanager.db.SchoolRoomDatabase
import ru.ak.schoolmanager.model.Student


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
//            binding.spResp1.setSelection((binding.spResp1.adapter as ArrayAdapter<String>).getPosition(student?.resp1))
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
        val resp1 = binding.spResp1.selectedItemPosition
        val respFio1 = binding.edRespFio1.text.toString()
        val respPhone1 = binding.edRespPhone1.text.toString()
        val resp2 = binding.spResp2.selectedItemPosition
        val respFio2 = binding.edRespFio2.text.toString()
        val respPhone2 = binding.edRespPhone2.text.toString()
        val note = binding.edNote.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val s = Student(fio, resp1, respFio1, respPhone1, resp2, respFio2, respPhone2, note)
            if (student == null) {
                SchoolRoomDatabase(this@AddStudentActivity).studentDao().addStudent(s)
            } else {
                s.id = student?.id ?: 0
                SchoolRoomDatabase(this@AddStudentActivity).studentDao().updateStudent(s)
            }
            finish()
        }
    }


}