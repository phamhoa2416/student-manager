package vn.edu.hust.studentman

import android.database.sqlite.SQLiteDatabase

object Students {
    lateinit var db: SQLiteDatabase

    val ref_list = listOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    val list: MutableList<StudentModel> = mutableListOf()

    val adapter = StudentAdapter(list)

    fun deleteStudent(id: String) {
        db.delete("students", "student_id = ?", arrayOf(id))
    }

    fun insertStudent(name: String, id: String) {
        db.execSQL("INSERT INTO students (name, student_id) VALUES ('$name', '$id')")
    }

    fun updateStudent(student: StudentModel, oldID: String) {
        db.update("students", student.toContentValues(), "student_id = ?", arrayOf(oldID))
    }
}
