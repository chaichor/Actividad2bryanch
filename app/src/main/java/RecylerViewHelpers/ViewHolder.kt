package RecylerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import christian.marin.actividad2bryanch.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtTicketDato)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
}