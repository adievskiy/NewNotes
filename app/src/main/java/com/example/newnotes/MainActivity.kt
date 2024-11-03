package com.example.newnotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), OnFragmentDataListener {
    private lateinit var db: DBHelper

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = DBHelper(this, null)

        val toolbarMain = findViewById<Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Заметки"

        val notesFragment = NotesFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, notesFragment).commit()


    }

    override fun onData(data: String) {
        val bundle = Bundle()
        bundle.putString("note", data)

        val transaction = this.supportFragmentManager.beginTransaction()
        val detailsFragment = DetailsFragment()
        detailsFragment.arguments = bundle

        transaction.replace(R.id.fragmentContainer, detailsFragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearDB -> db.removeDB()
            R.id.exitMenu -> finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }
}