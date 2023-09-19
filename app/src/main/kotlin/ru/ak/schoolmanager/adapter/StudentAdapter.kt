package ru.ak.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ak.schoolmanager.R
import ru.ak.schoolmanager.model.Student

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var data = mutableListOf<Student>()
    private var actionEdit: ((Student) -> Unit)? = null
    private var actionDelete: ((Student) -> Unit)? = null

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item_student_view_holder, parent, false)

        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = data[position]
        holder.tvFio.text = student.fio

        holder.actionEdit.setOnClickListener { actionEdit?.invoke(student) }
        holder.actionDelete.setOnClickListener { actionDelete?.invoke(student) }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(holder.itemView, position, student)
            }
        }
    }

    fun setData(list: List<Student>) {
        data.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setOnActionEditListener(callback: (Student) -> Unit) {
        this.actionEdit = callback
    }

    fun setOnActionDeleteListener(callback: (Student) -> Unit) {
        this.actionDelete = callback
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(view: View, position: Int, model: Student)
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFio: TextView = itemView.findViewById<TextView>(R.id.tvFio)
        val actionDelete: ImageView = itemView.findViewById(R.id.actDelete)
        val actionEdit: ImageView = itemView.findViewById(R.id.actEdit)
    }
}
