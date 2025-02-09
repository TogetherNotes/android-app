package com.example.togethernotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var navegation : BottomNavigationView
    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->

        when(item.itemId) {
            R.id.itemFragment1 -> {
                supportFragmentManager.commit {
                    replace<MatchFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                        addToBackStack("replacment")
                    }
                    return@OnNavigationItemSelectedListener true
                }

            }
        when(item.itemId) {
            R.id.itemFragment2 -> {
                supportFragmentManager.commit {
                    replace<ChatFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                        addToBackStack("replacment")
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        when(item.itemId) {
            R.id.itemFragment3 -> {
                supportFragmentManager.commit {
                    replace<calendarFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("replacment")
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        when(item.itemId) {
            R.id.itemFragment4 -> {
                supportFragmentManager.commit {
                    replace<AccountFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("replacment")
                }
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_menu_activity)
        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener(mOnNavMenu)

        supportFragmentManager.commit {
            replace<MatchFragment>(R.id.fragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack("replacment")
        }
    }
}