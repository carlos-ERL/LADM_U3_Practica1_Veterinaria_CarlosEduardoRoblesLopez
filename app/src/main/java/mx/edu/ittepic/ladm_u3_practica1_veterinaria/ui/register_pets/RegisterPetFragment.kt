package mx.edu.ittepic.ladm_u3_practica1_veterinaria.ui.register_pets

import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.Mascota
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.Propietario
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.R
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.databinding.FragmentRegisterPetBinding


class RegisterPetFragment : Fragment() {

    private var _binding: FragmentRegisterPetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterPetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpRaza
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.raza,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        mostrarDatosEnListView()

        binding.insertarMascota.setOnClickListener {
            var id = binding.txtcurp.text.toString()
            if (id == "") {
                AlertDialog.Builder(requireContext())
                    .setTitle("ATENCIÓN")
                    .setMessage("Debe agregar el propietario de la mascota")
                    .setNeutralButton("ACEPTAR") {d,i -> }
                    .show()
                return@setOnClickListener
            } else {
                if (binding.txtnombreMascota.text.toString() == "") {
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCIÓN")
                        .setMessage("Debe agregar el nombre de la mascota")
                        .setNeutralButton("ACEPTAR") {d,i -> }
                        .show()
                    return@setOnClickListener
                } else {
                    var mascota = Mascota(requireContext())

                    mascota.nombre = binding.txtnombreMascota.text.toString()
                    mascota.raza = binding.SpRaza.selectedItem.toString()
                    mascota.curp = binding.txtcurp.text.toString()

                    var resultado = mascota.insertar()
                    if(resultado) {
                        Toast.makeText(requireContext(),"SE INSERTO CON EXITO", Toast.LENGTH_LONG)
                            .show()
                        mostrarDatosEnListView()
                        limpiarCampos()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("ERROR")
                            .setMessage("NO SE PUDO INSERTAR")
                            .show()
                    }
                }
            }
        }
        binding.btnbuscar.setOnClickListener {
            var buscar = binding.txtbuscarPropietario.text.toString()
            mostrarPropietario(buscar)
        }
        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun mostrarPropietario(busqueda:String) {
        var listaFiltro = Propietario(requireContext()).buscarPropietario(busqueda)
        var listaData = ArrayList<String>()
        var DataPropietario = Propietario(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val persona = listaFiltro.get(it)
            DataPropietario.curp = persona.curp
            DataPropietario.nombre = persona.nombre
            DataPropietario.telefono = persona.telefono
            DataPropietario.edad = persona.edad

            listaData.add(DataPropietario.nombrePropietario())
            listaIDs.add(persona.curp)
        }

        binding.ownerList.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaData)
        try {
            binding.ownerList.setOnItemClickListener { adapterView, view, indice, l ->
                val curpLista = listaIDs.get(indice)
                val propietario = Propietario(requireContext()).mostrarPropietario(curpLista)

                AlertDialog.Builder(requireContext())
                    .setTitle("ATENCIÓN")
                    .setMessage("¿Qué deseas agregar al propietario: ${propietario.nombre}, \nTelefono: ${propietario.telefono}, \n" +
                            "CUPR: ${propietario.curp}?")
                    .setPositiveButton("Agregar") {d,i ->
                        binding.txtcurp.setText(propietario.curp)
                        binding.txtbuscarPropietario.setText("")
                    }
                    .setNeutralButton("Cerrar") {d,i -> }
                    .show()
            }
        } catch (err: SQLiteException) {
            AlertDialog.Builder(requireContext())
                .setTitle("ERROR")
                .setMessage(err.message)
                .setNeutralButton("Cerrar") {d,i -> }
                .show()
        }
    }

    fun mostrarDatosEnListView() {
        var listaMascotas = Mascota(requireContext()).mostrarTodos()
        var nombreMascota = ArrayList<String>()

        listaIDs.clear()
        (0..listaMascotas.size-1).forEach {
            val al = listaMascotas.get(it)
            nombreMascota.add(al.nombreMascota())
            listaIDs.add(al.id_mascota)
        }

        binding.listaMascotas.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,nombreMascota)
        binding.listaMascotas.setOnItemClickListener { adapterView, view, indice, l ->
            val idLista = listaIDs.get(indice)
            val pet = Mascota(requireContext()).mostrarMascota(idLista)

            AlertDialog.Builder(requireContext())
                .setTitle("ATENCIÓN")
                .setMessage("¿Qué deseas hacer con la \nMascota: ${pet.nombre} \n" +
                        "Raza: ${pet.raza} \nPropietario: ${pet.curp}?")
                .setNegativeButton("Eliminar") {d,i ->
                    pet.eliminar()
                    mostrarDatosEnListView()
                }
                .setPositiveButton("Actualizar") {d,i ->
                    /*
                               val otraVentana = Intent(requireActivity(), ActualizarMascotas::class.java)
                    otraVentana.putExtra("mascotaActualizar", pet.id_mascota)
                    startActivity(otraVentana)
                    * */
                }
                .setNeutralButton("Cerrar") {d,i -> }
                .show()
        }
    }

    fun limpiarCampos() {
        binding.txtbuscarPropietario.setText("")
        binding.txtcurp.setText("")
        binding.txtnombreMascota.setText("")
    }

    override fun onResume() {
        super.onResume()
        mostrarDatosEnListView()
    }
}