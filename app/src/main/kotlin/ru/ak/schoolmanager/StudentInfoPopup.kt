package ru.ak.schoolmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.PopupWindow
import ru.ak.schoolmanager.databinding.ActivityStudentInfoBinding
import ru.ak.schoolmanager.model.Student

class StudentInfoPopup (view: View, activity: Activity, student: Student) : PopupWindow(
    view,
    LinearLayout.LayoutParams.WRAP_CONTENT,
    LinearLayout.LayoutParams.WRAP_CONTENT
) {
    private var binding: ActivityStudentInfoBinding

    init {
        binding = ActivityStudentInfoBinding.inflate(activity.layoutInflater)
        isFocusable = true
        isTouchable = true
        isOutsideTouchable = true
        setBackgroundDrawable(null)

        val repArray = view.resources.getStringArray(R.array.responsible_list)
        binding.tvFio.text = student.fio
        if (student.resp1 != 0) {
            val sp1 = Html.fromHtml(
                "${repArray[student.resp1]}<br/>${student.respFio1}<br/><big>${student.respPhone1}</big>",
                FROM_HTML_MODE_LEGACY
            )
            binding.tvResp1.text = sp1
        } else {
            binding.tvResp1.visibility = GONE
        }
        if (student.resp2 != 0) {
            val sp2 = Html.fromHtml(
                "${repArray[student.resp2]}<br/>${student.respFio2}<br/><big>${student.respPhone2}</big>",
                FROM_HTML_MODE_LEGACY
            )
            binding.tvResp2.text = sp2
        } else {
            binding.tvResp2.visibility = GONE
        }
        if (student.note.isNotBlank()) {
            binding.tvNote.text = student.note
        } else {
            binding.tvNote.visibility = GONE
        }
        if (student.respPhone1.isNotBlank()) {
            binding.tvResp1.setOnClickListener {
                phoneCall(student.respPhone1, activity)
            }
        }
        if (student.respPhone2.isNotBlank()) {
            binding.tvResp2.setOnClickListener {
                phoneCall(student.respPhone2, activity)
            }
        }

        binding.root.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        height = binding.root.measuredHeight

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