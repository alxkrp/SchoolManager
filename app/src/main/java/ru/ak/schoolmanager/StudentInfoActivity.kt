package ru.ak.schoolmanager

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.ak.schoolmanager.databinding.ActivityStudentInfoBinding
import ru.ak.schoolmanager.model.Student

class StudentInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentInfoBinding
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        student = intent.getSerializableExtra("Data") as Student

        val reponsibles = resources.getStringArray(R.array.responsible_list)
        binding.tvFio.text = student?.fio
        binding.tvResp1.text = reponsibles.get(student?.resp1!!)
        binding.tvRespFio1.text = student?.respFio1
        binding.tvRespPhone1.text = student?.respPhone1
        binding.tvResp2.text = reponsibles.get(student?.resp2!!)
        binding.tvRespFio2.text = student?.respFio2
        binding.tvRespPhone2.text = student?.respPhone2
        binding.tvNote.text = student?.note

        binding.tvRespPhone1.setOnClickListener {
            phoneCall((it as TextView).text.toString())
        }
        binding.tvRespPhone2.setOnClickListener {
            phoneCall((it as TextView).text.toString())
        }
    }

    private fun phoneCall(phone: String) {
        val i: Intent = Intent(Intent.ACTION_CALL)
        i.setData(Uri.parse("tel:${phone}"))
        startActivity(i)
    }
}