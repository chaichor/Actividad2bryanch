package christian.marin.actividad2bryanch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogin = findViewById<Button>(R.id.btnLoguearse)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener{
            val pantallaLoginHelpDesk = Intent(this, login_helpDesk::class.java)
            startActivity(pantallaLoginHelpDesk)
        }

        btnRegister.setOnClickListener{
            val pantallaRegistroHelpDesk = Intent(this, registrarse_help_desk::class.java)
            startActivity(pantallaRegistroHelpDesk)
        }


    }
}