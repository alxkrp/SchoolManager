package ru.ak.schoolmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import ru.ak.schoolmanager.databinding.ActivityStudentInfoBinding
import ru.ak.schoolmanager.model.Student


class StudentInfoPopup : PopupWindow {
    private var binding: ActivityStudentInfoBinding

    constructor(view: View, activity: Activity, student: Student) :
            super(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) {
        binding = ActivityStudentInfoBinding.inflate(activity.layoutInflater)

        isFocusable = true
        isTouchable = true
        isOutsideTouchable = true

        val reponsibles = view.resources.getStringArray(R.array.responsible_list)
        binding.tvFio.text = student.fio
        binding.tvResp1.text = reponsibles.get(student.resp1)
        binding.tvRespFio1.text = student.respFio1
        binding.tvRespPhone1.text = student.respPhone1
        if (student.resp2 == 0) {
            binding.llResp2.visibility = GONE
        } else {
            binding.tvResp2.text = reponsibles.get(student.resp2)
            binding.tvRespFio2.text = student.respFio2
            binding.tvRespPhone2.text = student.respPhone2
        }
        if (student.note.isBlank()) {
            binding.tvNote.visibility = GONE
        } else {
            binding.tvNote.text = student.note
        }

        binding.tvRespPhone1.setOnClickListener {
            phoneCall((it as TextView).text.toString(), activity)
        }
        binding.tvRespPhone2.setOnClickListener {
            phoneCall((it as TextView).text.toString(), activity)
        }

        view.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        height = view.measuredHeight

        contentView = binding.root
    }

    fun show(view: View) {
        showAsDropDown(view, 20, 5)
    }

    private fun phoneCall(phone: String, activity: Activity) {
        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:${phone}")
        activity.startActivity(i)
    }
}