package com.ensoft.lesson36

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ensoft.lesson36.databinding.ActivityMainBinding
import com.ensoft.lesson36.db.DatabaseHelper
import com.ensoft.lesson36.db.Person

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var dbHelper: DatabaseHelper
    var list: MutableList<Person> = ArrayList()

    lateinit var rvAdapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        initializeList()

        rvAdapter = RvAdapter(list)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = rvAdapter

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerview.context,
                DividerItemDecoration.VERTICAL
            )
        )
        rvAdapter.setOnItemLongClickedListener(object : RvAdapter.OnItemClickListener{
            override fun onItemClickListener(position: Int) {
                showUpdateDialog(position)


            }

        })



        binding.btnSave.setOnClickListener {
            insertData()
        }
    }

    private fun showUpdateDialog(position: Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        val view  = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val etFirstName = view.findViewById<EditText>(R.id.et_firstName)
        val etLastName = view.findViewById<EditText>(R.id.et_lastName)
        val etAge = view.findViewById<EditText>(R.id.et_Age)
        val btnUpdate = view.findViewById<Button>(R.id.btn_update)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        builder.setView(view)

        etFirstName.setText(list[position].fName)
        etLastName.setText(list[position].lName)
        etAge.setText(list[position].age.toString())

        val dialog = builder.create()
        dialog.show()

        btnUpdate.setOnClickListener {
            updateData(list[position].id, etFirstName.text.toString(), etLastName.text.toString(), etAge.text.toString().toInt())
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            deleteData(list[position].id)
            dialog.dismiss()
        }


    }

    private fun insertData(){
        if (binding.etFName.text.isNotEmpty() && binding.etLName.text.isNotEmpty()
            &&binding.etAge.text.isNotEmpty()){
            val fName = binding.etFName.text.toString()
            val lName = binding.etLName.text.toString()
            val age = binding.etAge.text.toString().toInt()

            dbHelper.insertData(fName, lName, age)
            Toast.makeText(applicationContext, "Data saved successfully", Toast.LENGTH_SHORT).show()
            list.clear()
            binding.etFName.text.clear()
            binding.etLName.text.clear()
            binding.etAge.text.clear()
            initializeList()
            rvAdapter.notifyDataSetChanged()
        }
        else
            Toast.makeText(applicationContext, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
    }
    private fun updateData(id: String, fName: String, lName:String, age:Int){
        dbHelper.updateData(id, fName, lName, age)
        Toast.makeText(applicationContext, "Update successfully", Toast.LENGTH_SHORT).show()
        list.clear()
        initializeList()
        rvAdapter.notifyDataSetChanged()
    }

    private fun deleteData(id: String){
        val deleteId = id
        dbHelper.deleteData(deleteId)
        Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT).show()
        list.clear()
        initializeList()
        rvAdapter.notifyDataSetChanged()
    }

    private fun initializeList(){
        val cursor = dbHelper.readData()
        if (cursor.count > 0){
            while (cursor.moveToNext()){
                val id = cursor.getString(0)
                val fName = cursor.getString(1)
                val lName = cursor.getString(2)
                val age = cursor.getInt(3)

                val person = Person(id, fName, lName, age)
                list.add(person)
            }
        }
    }
}