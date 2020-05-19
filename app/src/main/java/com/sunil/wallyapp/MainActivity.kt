package com.sunil.wallyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // This line is only necessary if using the default action bar.
        setupActionBarWithNavController(navController, appBarConfiguration)

        // This remaining block is only necessary if using a Toolbar from your layout.
        toolbar.setupWithNavController(navController, appBarConfiguration)
        // This will handle back actions initiated by the the back arrow
        // at the start of the toolbar.
        toolbar.setNavigationOnClickListener {
            // Handle the back button event and return to override
            // the default behavior the same way as the OnBackPressedCallback.
            // TODO(reason: handle custom back behavior here if desired.)

            // If no custom behavior was handled perform the default action.
            navController.navigateUp() || super.onSupportNavigateUp()
        }

    }
}
