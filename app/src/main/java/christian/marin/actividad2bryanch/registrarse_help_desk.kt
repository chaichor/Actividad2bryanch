package christian.marin.actividad2bryanch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

class registrarse_help_desk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse_help_desk)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNameRegister = findViewById<EditText>(R.id.txtNameRegister)
        val txtPasswordRegister = findViewById<EditText>(R.id.txtPasswordRegister)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val btnBackRegister = findViewById<Button>(R.id.btnBackRegister)


        btnBackRegister.setOnClickListener{
            val pantallaPrincipal = Intent(this, MainActivity::class.java)
            startActivity(pantallaPrincipal)
        }

        btnRegistrarse.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = ClaseConexion().cadenaConexion()

                val crearUsuario = objConexion?.prepareStatement("INSERT INTO usuario (UUID_user, name, password) VALUES (?, ?, ?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, txtNameRegister.text.toString())
                crearUsuario.setString(3, txtPasswordRegister.text.toString())
                crearUsuario.executeUpdate()


                withContext(Dispatchers.Main) {
                    Toast.makeText(this@registrarse_help_desk, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    txtNameRegister.setText("")
                    txtPasswordRegister.setText("")
                }
            }
        }

    }
}