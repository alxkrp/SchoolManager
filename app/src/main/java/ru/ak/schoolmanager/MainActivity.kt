package ru.ak.schoolmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ak.schoolmanager.adapter.StudentAdapter
import ru.ak.schoolmanager.databinding.ActivityMainBinding
import ru.ak.schoolmanager.db.SchoolRoomDatabase
import ru.ak.schoolmanager.model.Student


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mAdapter: StudentAdapter? = null

    val PERMISSION_CODE: Int  = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun phoneCall() {
        val i: Intent = Intent(Intent.ACTION_CALL)
        i.setData(Uri.parse("tel:+79788375413"))
        startActivity(i)
    }

    private fun setAdapter(list: List<Student>) {
        mAdapter?.setData(list)
    }

    override fun onResume() {
        super.onResume()

        mAdapter = StudentAdapter()


        CoroutineScope(Dispatchers.IO).launch {
            val studentList = SchoolRoomDatabase(this@MainActivity).studentDao().getStudentsAll()
            Handler(Looper.getMainLooper()).post(Runnable {
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
                            CoroutineScope(Dispatchers.IO).launch {
                                SchoolRoomDatabase(this@MainActivity).studentDao().deleteStudent(it)
                                val list = SchoolRoomDatabase(this@MainActivity).studentDao().getStudentsAll()
                                Handler(Looper.getMainLooper()).post(Runnable {
                                    setAdapter(list)
                                })
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
            })
        }
    }

    private fun showStudentInfo(model: Student) {
        val intent = Intent(this@MainActivity, StudentInfoActivity::class.java)
        intent.putExtra("Data", model)
        startActivity(intent)
    }
}