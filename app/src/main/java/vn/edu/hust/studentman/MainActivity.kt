package vn.edu.hust.studentman

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
  private lateinit var root: ConstraintLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    root = findViewById(R.id.main)

    Students.db = openOrCreateDatabase(
      filesDir.path + "/student.db",
      MODE_PRIVATE,
      null)

    Students.db.execSQL(
        "CREATE TABLE IF NOT EXISTS students (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "student_id TEXT UNIQUE);"
    )

    addData()
    retrieveData()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_options, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_add -> {
        findNavController(R.id.fragment_view).navigate(R.id.updateFragment, bundleOf("state" to "add"))
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }

  override fun onDestroy() {
    Students.db.execSQL("DROP TABLE IF EXISTS students")
    Students.db.close()
    super.onDestroy()
  }

  private fun addData() {
    var count = 0
    Students.db.rawQuery("SELECT * FROM students", null).use { cursor ->
      while (cursor.moveToNext()) count++
    }
    if (count < Students.ref_list.size) {
      Students.ref_list.forEach {
        try {
          Students.db.execSQL("INSERT INTO students (name, student_id) VALUES ('${it.studentName}', '${it.studentId}');")
        } catch (e: Exception) {
            e.toString()
        }
      }
    }
  }

  private fun retrieveData() {
    Students.list.clear()
    Students.db.rawQuery("SELECT * FROM students", null).use { cursor ->
      while (cursor.moveToNext()) {
        val name = cursor.getString(1)
        val id = cursor.getString(2)
        Students.list.add(StudentModel(name, id))
      }
    }
  }
}