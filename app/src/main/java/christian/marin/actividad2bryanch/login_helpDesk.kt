package christian.marin.actividad2bryanch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion

class login_helpDesk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_help_desk)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtName = findViewById<EditText>(R.id.txtName)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLoguearse = findViewById<Button>(R.id.btnLoguearse)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener{
            val pantallaPrincipal = Intent(this, MainActivity::class.java)
            startActivity(pantallaPrincipal)
        }

        btnLoguearse.setOnClickListener {
            val pantallaCrearTicket = Intent(this, crearticket::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM usuario WHERE name = ? AND password = ?")!!
                comprobarUsuario.setString(1, txtName.text.toString())
                comprobarUsuario.setString(2, txtPassword.text.toString())
                val resultado = comprobarUsuario.executeQuery()

                if (resultado.next()) {
                    startActivity(pantallaCrearTicket)
                }else{
                    println("Usuario no encontrado, verifique las credenciales")
                }
            }
        }

    }
}