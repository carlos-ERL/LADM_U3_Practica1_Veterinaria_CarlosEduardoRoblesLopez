package mx.edu.ittepic.ladm_u3_practica1_veterinaria

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Mascota (este: Context) {
    var este = este
    var id_mascota = ""
    var nombre = ""
    var raza = ""
    var curp = ""
    var err = ""

    fun insertar() : Boolean {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        try {
            val tabla = baseDatos.writableDatabase

            var datos = ContentValues()

            datos.put("NOMBRE",nombre)
            datos.put("RAZA",raza)
            datos.put("CURP_OW",curp)

            var resultado = tabla.insert("MASCOTA",null,datos)
            if (resultado == -1L) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }

    fun actualizar() : Boolean {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("ID_MASCOTA",id_mascota)
            datosActualizados.put("NOMBRE",nombre)
            datosActualizados.put("RAZA",raza)
            datosActualizados.put("CURP_OW",curp)

            val resultado = tabla.update("MASCOTA",datosActualizados,"ID_MASCOTA=?", arrayOf(id_mascota))
            if (resultado == 0) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }

    fun eliminar() : Boolean {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val respuesta = tabla.delete("MASCOTA","ID_MASCOTA=?", arrayOf(id_mascota))

            if (respuesta == 0) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }

    fun mostrarTodos() : ArrayList<Mascota> {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        var arreglo = ArrayList<Mascota>()

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM MASCOTA"


            var cursor = tabla.rawQuery(SQL_SELECT,null)
            if (cursor.moveToFirst()) {
                do {
                    val mascotas = Mascota(este)
                    mascotas.id_mascota = cursor.getString(0)
                    mascotas.nombre = cursor.getString(1)
                    mascotas.raza = cursor.getString(2)
                    mascotas.curp = cursor.getString(3)
                    arreglo.add(mascotas)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarMascota(buscar:String) : Mascota {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        val mascota = Mascota(este)

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM MASCOTA WHERE ID_MASCOTA=?"

            var cursor = tabla.rawQuery(SQL_SELECT, arrayOf(buscar))
            if (cursor.moveToFirst()) {
                mascota.id_mascota = cursor.getString(0)
                mascota.nombre = cursor.getString(1)
                mascota.raza = cursor.getString(2)
                mascota.curp = cursor.getString(3)
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return mascota
    }

    fun buscarFiltroMascota(busqueda:String,filtro:String) : ArrayList<Mascota> {
        var baseDatos = BaseDatos(este, "mascotas", null, 1)
        err = ""
        var arreglo = ArrayList<Mascota>()
        var bus = busqueda
        var fil = filtro
        var SQL_SELECT = ""

        try {
            when (fil) {
                "PROPIETARIO(curp)" -> {
                    SQL_SELECT = "SELECT * FROM MASCOTA WHERE CURP_OW LIKE '${bus}%'"
                }
                "NOMBRE MASCOTA" -> {
                    SQL_SELECT = "SELECT * FROM MASCOTA WHERE NOMBRE LIKE '${bus}%' OR NOMBRE LIKE '%${bus}%'"
                }
                "RAZA" -> {
                    SQL_SELECT = "SELECT * FROM MASCOTA WHERE RAZA LIKE '${bus}%'"
                }
            }
            var tabla = baseDatos.readableDatabase

            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val mascota = Mascota(este)
                    mascota.id_mascota = cursor.getString(0)
                    mascota.nombre = cursor.getString(1)
                    mascota.raza = cursor.getString(2)
                    mascota.curp = cursor.getString(3)
                    arreglo.add(mascota)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun nombreMascota() : String {
        return nombre+", "+raza+", "+curp
    }
}