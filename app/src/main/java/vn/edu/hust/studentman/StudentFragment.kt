package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentFragment : Fragment() {
    private val studentAdapter = Students.adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler_view_students).run {
          addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
          registerForContextMenu(this)
          adapter = studentAdapter
          layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = StudentAdapter.selectedPos

        val builder = requireContext().let {
            AlertDialog.Builder(it)
                .setIcon(R.drawable.baseline_warning_amber_24)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student?")
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        }

        val dialog: AlertDialog = builder.setPositiveButton("Yes") { dialog, _ ->
            val pos: Int = position
            val student: StudentModel? = if (pos != RecyclerView.NO_POSITION) Students.list[pos] else null
            if (pos != RecyclerView.NO_POSITION) {
                studentAdapter.removeStudent(pos)
                dialog.dismiss()
                this.view?.let {
                    Snackbar.make(it, "Student deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            Students.list.add(pos, student!!)
                            Students.insertStudent(student.studentName, student.studentId)
                            studentAdapter.notifyItemInserted(pos)
                        }.show()
                }
            }
        }.create()

        return when (item.itemId) {
            R.id.edit -> {
                val bundle = bundleOf(
                    "name" to Students.list[position].studentName,
                    "id" to Students.list[position].studentId,
                    "position" to position,
                    "state" to "update"
                )
                findNavController().navigate(R.id.update_student_details, bundle)
                true
            }
            R.id.delete -> {
                dialog.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}