package mx.edu.ittepic.ladm_u3_practica1_veterinaria.ui.search_owners


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.Propietario
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.R
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.databinding.FragmentSearchOwnersBinding

class SearchOwnerFragment : Fragment() {
    private var _binding: FragmentSearchOwnersBinding? = null

    private val binding get() = _binding!!

    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchOwnersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpConsultasPropietario
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buscarProPor,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        mostrarDatosEnListView()

        binding.btnBuscar.setOnClickListener {
            var busqueda = binding.txtbuscar.text.toString()

            if (busqueda == "") {
                mostrarDatosEnListView()
            } else {
                mostrarPropietariosFiltro(busqueda,binding.SpConsultasPropietario.selectedItem.toString())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun mostrarDatosEnListView() {
        var listaFiltro = Propietario(requireContext()).mostrarTodos()
        var listaData = ArrayList<String>()
        var DataPropietario = Propietario(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val persona = listaFiltro.get(it)
            DataPropietario.curp = persona.curp
            DataPropietario.nombre = persona.nombre
            DataPropietario.telefono = persona.telefono
            DataPropietario.edad = persona.edad

            listaData.add(DataPropietario.contenido())
            listaIDs.add(persona.curp)
        }

        binding.owners.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaData)
    }

    fun mostrarPropietariosFiltro(busqueda:String, filtro:String) {
        var listaFiltro = Propietario(requireContext()).mostrarFiltro(busqueda,filtro)
        var listaData = ArrayList<String>()
        var DataPropietario = Propietario(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val persona = listaFiltro.get(it)
            DataPropietario.curp = persona.curp
            DataPropietario.nombre = persona.nombre
            DataPropietario.telefono = persona.telefono
            DataPropietario.edad = persona.edad

            listaData.add(DataPropietario.contenido())
            listaIDs.add(persona.curp)
        }

        binding.owners.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaData)
    }

}