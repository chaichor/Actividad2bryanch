package christian.marin.actividad2bryanch

import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import christian.marin.actividad2bryanch.R.id.txtSdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaTickets
import java.util.UUID

class crearticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crearticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtSdate = findViewById<EditText>(R.id.txtSdate)
        val txtEstatus = findViewById<EditText>(R.id.txtEstatus)
        val txtFdate = findViewById<EditText>(R.id.txtFdate)
        val btnCrearTicket = findViewById<Button>(R.id.btnCrearTicket)
        val rcvDatos = findViewById<RecyclerView>(R.id.rcvDatos)

        rcvDatos.layoutManager = LinearLayoutManager(this)

        fun obtenerDatos(): List<listaTickets> {

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tickets")!!

            val listadoTickets = mutableListOf<listaTickets>()

            while (resultSet.next()) {
                val uuid = resultSet.getString("UUID_tickets")
                val titulo = resultSet.getString("titulo")
                val descripcion = resultSet.getString("descripcion")
                val autor = resultSet.getString("autor")
                val email = resultSet.getString("email")
                val cdate = resultSet.getString("cdate")
                val staus = resultSet.getString("status")
                val fdate = resultSet.getString("fdate")
                val ticket = listaTickets(uuid, titulo, descripcion, autor, email, cdate, staus, fdate)

                listadoTickets.add(ticket)
            }

            return listadoTickets

        }

        //ejecutar la funcion

        CoroutineScope(Dispatchers.IO).launch {
            val ejecutarFuncion = obtenerDatos()

            withContext(Dispatchers.Main){
                //Asigno el adaptador mi RecyclerView
                //(Uno mi Adaptador con el RecyclerView)
                val miAdaptador = Adaptador(ejecutarFuncion)
                rcvDatos.adapter = miAdaptador
            }
        }

        btnCrearTicket.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = ClaseConexion().cadenaConexion()

                val crearTicket = objConexion?.prepareStatement("INSERT INTO tickets (UUID_tickets, titulo, descripcion, autor, email, cdate, status, fdate) VALUES (?,?,?,?,?,?,?,?)")!!
                crearTicket.setString(1, UUID.randomUUID().toString())
                crearTicket.setString(2, txtTitulo.text.toString())
                crearTicket.setString(3, txtDescripcion.text.toString())
                crearTicket.setString(4, txtAutor.text.toString())
                crearTicket.setString(5, txtEmail.text.toString())
                crearTicket.setString(6, txtSdate.text.toString())
                crearTicket.setString(7, txtEstatus.text.toString())
                crearTicket.setString(8, txtFdate.text.toString())
                crearTicket.executeUpdate()

                val nuevosTickets = obtenerDatos()

                withContext(Dispatchers.Main){
                    (rcvDatos.adapter as? Adaptador)?.actualizarRecyclerView(nuevosTickets)
                    txtTitulo.text.clear()
                    txtDescripcion.text.clear()
                    txtAutor.text.clear()
                    txtEmail.text.clear()
                    txtSdate.text.clear()
                    txtEstatus.text.clear()
                    txtFdate.text.clear()
                }
            }
        }

    }
}