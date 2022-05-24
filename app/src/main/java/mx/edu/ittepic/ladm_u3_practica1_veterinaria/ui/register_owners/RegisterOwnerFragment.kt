package mx.edu.ittepic.ladm_u3_practica1_veterinaria.ui.register_owners

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.Propietario
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.databinding.FragmentRegisterOwnerBinding

class RegisterOwnerFragment : Fragment() {
    private var _binding: FragmentRegisterOwnerBinding? = null
    var listaIDs = ArrayList<String>()
    var curp = ""
    var updateFlag = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterOwnerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showData()
        binding.insertar.setOnClickListener {
            if(updateFlag == 0){

                val c0 = binding.ownerCurp.text.toString()
                val c1 = binding.ownerName.text.toString()
                val c2 = binding.ownerPhone.text.toString()
                val c3 = binding.ownerAge.text.toString()

                val regex = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$".toRegex()

                if (!(c0 == "" || c1 == "" || c2 == "" || c3 == "")) {
                    if(!(c2.length == 10)) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("TELEFONO")
                            .setMessage("EL NÚMERO DEBEN SER 10 DIGITOS")
                            .setNeutralButton("ACEPTAR") {d,i -> }
                            .show()
                    } else {
                        var propietario = Propietario(requireContext())

                        propietario.curp = binding.ownerCurp.text.toString()
                        propietario.nombre = binding.ownerName.text.toString()
                        propietario.telefono = binding.ownerPhone.text.toString()
                        propietario.edad = binding.ownerAge.text.toString().toInt()

                        var resultado = propietario.insertar()
                        if(resultado) {
                            Toast.makeText(requireContext(),"SE INSERTO CON EXITO", Toast.LENGTH_LONG)
                                .show()
                            showData()
                            limpiarCampos()
                        } else {
                            AlertDialog.Builder(requireContext())
                                .setTitle("ERROR")
                                .setMessage("NO SE PUDO INSERTAR")
                                .show()
                        }
                    }
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCIÓN")
                        .setMessage("HAY CAMPOS VACIOS")
                        .show()
                }
            }else{
                var propietario = Propietario(requireContext())

                try {
                } catch (e:Exception) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCIÓN")
                        .setMessage("HAY CAMPOS VACIOS")
                        .show()
                    return@setOnClickListener
                }
                val regex =
                    "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$".toRegex()
                try {
                    if (!(propietario.curp == "" || propietario.nombre == "" || propietario.telefono == "" || propietario.edad.toString() == "")) {
                       if (!(propietario.telefono.length == 10)) {
                            AlertDialog.Builder(requireContext())
                                .setTitle("TELEFONO")
                                .setMessage("EL NÚMERO DEBEN SER 10 DIGITOS")
                                .setNeutralButton("ACEPTAR") { d, i -> }
                                .show()
                        } else {
                            var respuesta = propietario.actualizar()

                            if (respuesta) {
                                Toast.makeText(requireContext(), "SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                                    .show()
                                limpiarCampos()

                            } else {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("ERROR")
                                    .setMessage("NO SE PUDO ACTUALIZAR")
                                    .show()
                            }
                        }
                    }
                }  catch(e:Exception) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCIÓN")
                        .setMessage("HAY CAMPOS VACIOS")
                        .show()
                }

            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showData() {
        var listaPropietarios = Propietario(requireContext()).mostrarTodos()
        var nombrePropietarios = ArrayList<String>()

        listaIDs.clear()
        (0..listaPropietarios.size-1).forEach {
            val al = listaPropietarios.get(it)
            nombrePropietarios.add(al.nombre)
            listaIDs.add(al.curp)
        }

        binding.owners.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1,nombrePropietarios)
        binding.owners.setOnItemClickListener { adapterView, view, indice, l ->
            val curpLista = listaIDs.get(indice)
            updateFlag = 1
            val propietario = Propietario(requireContext()).mostrarPropietario(curpLista)

            AlertDialog.Builder(requireContext())
                .setTitle("ATENCIÓN")
                .setMessage("¿Qué deseas hacer con ${propietario.nombre}, \nTelefono: ${propietario.telefono}, \n" +
                        "Edad: ${propietario.edad}?")
                .setNegativeButton("Eliminar") {d,i ->
                    propietario.eliminar()
                    showData()
                }
                .setPositiveButton("Actualizar") {d,i ->
                    binding.ownerCurp.setText(propietario.curp.toString())
                    binding.ownerName.setText(propietario.nombre.toString())
                    binding.ownerPhone.setText(propietario.telefono.toString())
                    binding.ownerAge.setText(propietario.edad.toString())
                    binding.insertar.setHint("Actualizar")
                    binding.insertar.setText("Actualizar")
                    updateFlag = 1

                }
                .setNeutralButton("Cerrar") {d,i -> }
                .show()
        }
    }

    fun limpiarCampos() {
        binding.ownerCurp.setText("")
        binding.ownerName.setText("")
        binding.ownerPhone.setText("")
        binding.ownerAge.setText("")
        binding.insertar.setHint("REGISTRAR")
        binding.insertar.setText("REGISTRAR")
        updateFlag = 0
        showData()
    }

    override fun onResume() {
        super.onResume()
        showData()
    }
}