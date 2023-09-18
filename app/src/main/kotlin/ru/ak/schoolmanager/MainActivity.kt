package ru.ak.schoolmanager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.ak.schoolmanager.adapter.StudentAdapter
import ru.ak.schoolmanager.databinding.ActivityMainBinding
import ru.ak.schoolmanager.model.Student


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { MainViewModel(Dependencies.statisticRepository) }
    private var mAdapter: StudentAdapter? = null

    private val PERMISSION_CODE: Int  = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CODE)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStudentActivity::class.java)
            startActivity(intent)
        }

        binding.btnCall.setOnClickListener {
            phoneCall()
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // открываем поток на чтение по полученному URI
            val intent = result.data
            val myFile = contentResolver.openInputStream(intent?.data!!)
            if (myFile != null) {

                // читаем данные
                val content = myFile.bufferedReader().readText()
                // демонстрируем имя файла и объем прочитанных данных
                Toast
                    .makeText(this, "File %s, Length %d bytes".format(intent.data!!.path, content.length), Toast.LENGTH_LONG)
                    .show()
                loadJson(content)
            }
        }
    }

    private fun loadJson(json: String) {
        val obj = JSONObject(json)
        val arr = obj.getJSONArray("students")
        for (i in 0 until arr.length()) {
            val studentJson = arr.getJSONObject(i)
            val fio = studentJson.optString("fio")
            if (!fio.isNullOrBlank()) {
                val student = Student(
                    studentJson.getString("fio"),
                    studentJson.optInt("resp1"),
                    studentJson.optString("respFio1"),
                    studentJson.optString("respPhone1"),
                    studentJson.optInt("resp2"),
                    studentJson.optString("respFio2"),
                    studentJson.optString("respPhone2"),
                    studentJson.optString("note"),
                )
                viewModel.addStudent(student)
            }
        }
    }

    private fun phoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }

        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        // запускаем контракт
        startForResult.launch(intent)
    }


    private fun setAdapter(list: List<Student>) {
        mAdapter?.setData(list)
    }

    override fun onResume() {
        super.onResume()

        mAdapter = StudentAdapter()

        CoroutineScope(Dispatchers.IO).launch {
            val studentList = viewModel.getStudentsAll()
            lifecycleScope.launch {
                binding.rvStudents.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = mAdapter
                    setAdapter(studentList)

                    mAdapter?.setOnClickListener(object :
                        StudentAdapter.OnClickListener {
                            override fun onClick(position: Int, model: Student) {
                                showStudentInfo(model)
                            }
                        }
                    )

                    mAdapter?.setOnActionEditListener {
                        val intent = Intent(this@MainActivity, AddStudentActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)
                    }
                    mAdapter?.setOnActionDeleteListener {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("Вы уверены, что хотите удалить ученика?")
                        builder.setPositiveButton("Да") { p0, _ ->
                            viewModel.deleteStudent(it.id)
                            CoroutineScope(Dispatchers.IO).launch {
                                val list = viewModel.getStudentsAll()
                                lifecycleScope.launch {
                                    setAdapter(list)
                                }
                            }
                            p0.dismiss()
                        }
                        builder.setNegativeButton("Нет") { p0, _ ->
                            p0.dismiss()
                        }

                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
    }

    private fun showStudentInfo(model: Student) {
        val intent = Intent(this@MainActivity, StudentInfoActivity::class.java)
        intent.putExtra("Data", model)
        startActivity(intent)
    }
}