package mx.edu.ittepic.ladm_u3_practica1_veterinaria

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException


class Propietario (este: Context) {
    var este = este
    var curp = ""
    var nombre = ""
    var telefono = ""
    var edad = 0
    var err = ""
    var contador = 0

    fun insertar() : Boolean {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        try {
            val tabla = baseDatos.writableDatabase

            var datos = ContentValues()

            datos.put("CURP", curp)
            datos.put("NOMBRE",nombre)
            datos.put("TELEFONO",telefono)
            datos.put("EDAD",edad)

            var resultado = tabla.insert("PROPIETARIO",null,datos)
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

    fun mostrarTodos() : ArrayList<Propietario> {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM PROPIETARIO"


            var cursor = tabla.rawQuery(SQL_SELECT,null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(este)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarPropietario(curpBuscar:String) : Propietario {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        val propietario = Propietario(este)

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE CURP=?"

            var cursor = tabla.rawQuery(SQL_SELECT, arrayOf(curpBuscar))
            if (cursor.moveToFirst()) {
                propietario.curp = cursor.getString(0)
                propietario.nombre = cursor.getString(1)
                propietario.telefono = cursor.getString(2)
                propietario.edad = cursor.getString(3).toInt()
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return propietario
    }

    fun countMascotas(curpBuscar: String) : Int {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""

        try {
            var tabla = baseDatos.readableDatabase
            //var SQL_SELECT = "SELECT * FROM MASCOTA INNER JOIN PROPIETARIO ON MASCOTA.CURP_PRO = PROPIETARIO.CURP WHERE PROPIETARIO.CURP = '${curpBuscar}'"
            var SQL_SELECT = "select count(*) from PROPIETARIO inner join MASCOTA on  PROPIETARIO.CURP = MASCOTA.CURP_PRO where MASCOTA.CURP_PRO = '${curpBuscar}'"
            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                contador = cursor.getInt(0)
            }

        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return contador
    }

    fun buscarPropietario(nombreApeBuscar:String) : ArrayList<Propietario> {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var buscar = nombreApeBuscar

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${buscar}%' OR NOMBRE LIKE '%${buscar}%'"

            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(este)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarFiltro(busqueda:String,filtro:String) : ArrayList<Propietario> {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var bus = busqueda
        var fil = filtro
        var SQL_SELECT = ""

        try {
            when (fil) {
                "CURP" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE CURP LIKE '${bus}%'"
                }
                "NOMBRE" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${bus}%' OR NOMBRE LIKE '%${bus}%'"
                }
                "TELEFONO" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE TELEFONO LIKE '${bus}%'"
                }
                "EDAD" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE EDAD LIKE '${bus}%'"
                }
            }
            var tabla = baseDatos.readableDatabase

            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(este)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun actualizar() : Boolean {
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("NOMBRE",nombre)
            datosActualizados.put("TELEFONO",telefono)
            datosActualizados.put("EDAD",edad)

            val resultado = tabla.update("PROPIETARIO",datosActualizados,"CURP=?", arrayOf(curp))
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
        var baseDatos = BaseDatos(este, "propietarios", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val respuesta = tabla.delete("PROPIETARIO","CURP=?", arrayOf(curp))

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

    fun contenido() : String {
        return "CURP: ${curp}\nNOMBRE: ${nombre}\nTELEFONO: ${telefono}\nEDAD: ${edad}"
    }

    fun nombrePropietario() : String {
        return nombre+", "+telefono
    }
}