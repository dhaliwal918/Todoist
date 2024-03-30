package com.example.todoist

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.todoist.R.layout.todo_list_view_layout
class CustomAdapter (val context : Activity , val arrayList: ArrayList<TaskData>) : ArrayAdapter<TaskData>(context , todo_list_view_layout , arrayList) {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(todo_list_view_layout , null)

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtTime = view.findViewById<TextView>(R.id.txtTime)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtPriority = view.findViewById<TextView>(R.id.txtSetPriority)
        val priorityColor = view.findViewById<ImageView>(R.id.priorityColor)

        txtTitle.text = arrayList[position].title
        txtDescription.text = arrayList[position].description
        txtDate.text = arrayList[position].dueDate
        txtTime.text = arrayList[position].dueTime
        txtPriority.text = arrayList[position].priority
        if(arrayList[position].priority=="high"){
            priorityColor.setImageResource(R.drawable.red)
        }else if(arrayList[position].priority=="medium"){
            priorityColor.setImageResource(R.drawable.yellow)
        }else
            priorityColor.setImageResource(R.drawable.green)

        return view
    }
}

