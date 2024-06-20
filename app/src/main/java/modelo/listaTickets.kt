package modelo

data class listaTickets (

    val uuid: String,
    var titulo: String,
    val descripcion: String,
    val autor: String,
    val email: String,
    val cdate: String,
    val staus: String,
    val fdate: String

)
