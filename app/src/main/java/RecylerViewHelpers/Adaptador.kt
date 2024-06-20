package RecyclerViewHelpers

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import christian.marin.actividad2bryanch.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.listaTickets

class Adaptador(private var Datos: List<listaTickets>): RecyclerView.Adapter<Adaptador.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtTicketDato)
        val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
        val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    }

    fun actualizarRecyclerView(nuevaLista: List<listaTickets>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.textView.text = ticket.titulo

        holder.imgBorrar.setOnClickListener {
            val context = holder.textView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Estás seguro de eliminar el ticket?")

            builder.setPositiveButton("Si") { dialog, _ ->
                eliminarRegistro(ticket.titulo, position)
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }

        holder.imgEditar.setOnClickListener {
            val context = holder.textView.context

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editar_ticket, null)
            val edtTitulo = dialogView.findViewById<EditText>(R.id.edtTitulo)
            val edtDescripcion = dialogView.findViewById<EditText>(R.id.edtDescripcion)
            val edtAutor = dialogView.findViewById<EditText>(R.id.edtAutor)
            val edtEmail = dialogView.findViewById<EditText>(R.id.edtEmail)
            val edtCdate = dialogView.findViewById<EditText>(R.id.edtCdate)
            val edtStatus = dialogView.findViewById<EditText>(R.id.edtStatus)
            val edtFdate = dialogView.findViewById<EditText>(R.id.edtFdate)


            edtTitulo.setText(ticket.titulo)
            edtDescripcion.setText(ticket.descripcion)
            edtAutor.setText(ticket.autor)
            edtEmail.setText(ticket.email)
            edtCdate.setText(ticket.cdate)
            edtStatus.setText(ticket.staus)
            edtFdate.setText(ticket.fdate)

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Ticket")
            builder.setView(dialogView)
            builder.setPositiveButton("Guardar") { dialog, _ ->

                val nuevoTitulo = edtTitulo.text.toString()
                val nuevaDescripcion = edtDescripcion.text.toString()
                val nuevoAutor = edtAutor.text.toString()
                val nuevoEmail = edtEmail.text.toString()
                val nuevaCdate = edtCdate.text.toString()
                val nuevoStatus = edtStatus.text.toString()
                val nuevaFdate = edtFdate.text.toString()


                val ticketEditado = listaTickets(
                    uuid = ticket.uuid,
                    titulo = nuevoTitulo,
                    descripcion = nuevaDescripcion,
                    autor = nuevoAutor,
                    email = nuevoEmail,
                    cdate = nuevaCdate,
                    staus = nuevoStatus,
                    fdate = nuevaFdate
                )

                // Actualiza el ticket en la base de datos
                editarTicket(ticketEditado)
                actualizarListadoDespuesDeEditar(ticketEditado.uuid, ticketEditado.titulo)
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    fun eliminarRegistro(titulo: String, position: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)
        actualizarRecyclerView(listaDatos)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val borrarTicket = objConexion?.prepareStatement("DELETE FROM tickets WHERE titulo =?")!!
            borrarTicket.setString(1, titulo)
            borrarTicket.executeUpdate()

            val commit = objConexion?.prepareStatement("COMMIT")!!
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun actualizarListadoDespuesDeEditar(uuid: String, nuevoTitulo: String) {
        val identificador = Datos.indexOfFirst { it.uuid == uuid }

        if (identificador != -1) {
            Datos[identificador].titulo = nuevoTitulo
            notifyItemChanged(identificador)
        }
    }

    fun editarTicket(ticketEditado: listaTickets) {
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val updateTicket = objConexion?.prepareStatement(
                "UPDATE tickets SET titulo =?, descripcion =?, autor =?, email =?, cdate =?, status =?, fdate =? WHERE UUID_tickets =?"
            )!!
            updateTicket.setString(1, ticketEditado.titulo)
            updateTicket.setString(2, ticketEditado.descripcion)
            updateTicket.setString(3, ticketEditado.autor)
            updateTicket.setString(4, ticketEditado.email)
            updateTicket.setString(5, ticketEditado.cdate)
            updateTicket.setString(6, ticketEditado.staus)
            updateTicket.setString(7, ticketEditado.fdate)
            updateTicket.setString(8, ticketEditado.uuid)
            updateTicket.executeUpdate()

            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()
        }
    }

}
