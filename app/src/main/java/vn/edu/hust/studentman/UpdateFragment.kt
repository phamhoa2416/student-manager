package vn.edu.hust.studentman

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class UpdateFragment : Fragment() {
    private var name: String = ""
    private var id: String = ""
    private var state: String = "add"
    private var pos: Int = -1

    private val studentAdapter = Students.adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name") ?: name
            id = it.getString("id") ?: id
            state = it.getString("state") ?: state
            pos = it.getInt("position", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_student, container, false)
    }

    private fun handleException(e: Exception) {
        Log.d("UpdateFragment", e.message ?: "An error occurred")
        val message = e.message.let {
            if (it?.contains("UNIQUE constraint failed") == true) {
                "Student ID already exists"
            } else {
                it
            }
        } ?: "An error occurred"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (name.isNotBlank()) {
            view.findViewById<EditText>(R.id.edit_student_name).setText(name)
        }

        if (id.isNotBlank()) {
            view.findViewById<EditText>(R.id.edit_student_id).setText(id)
        }

        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            findNavController().popBackStack()
        }

        view.findViewById<Button>(R.id.btn_update).text = if (state == "add") "Add" else "Update"

        view.findViewById<Button>(R.id.btn_update).setOnClickListener {
            val newName = view.findViewById<EditText>(R.id.edit_student_name).text.toString()
            val newID = view.findViewById<EditText>(R.id.edit_student_id).text.toString()
            if (state == "add") {
                try {
                    Students.insertStudent(newName, newID)
                } catch (e: SQLiteConstraintException) {
                    handleException(e)
                    return@setOnClickListener
                }
                Students.list.add(StudentModel(newName, newID))
                studentAdapter.notifyItemInserted(Students.list.size - 1)
            } else if (state == "update") {
                if (pos != RecyclerView.NO_POSITION) {
                    val newStudent = StudentModel(newName, newID)
                    try {
                        Students.updateStudent(newStudent, id)
                    } catch (e: SQLiteConstraintException) {
                        handleException(e)
                        return@setOnClickListener
                    }
                    Students.list[pos] = newStudent
                    studentAdapter.notifyItemChanged(pos)
                }
            }
            findNavController().popBackStack()
        }
    }
}