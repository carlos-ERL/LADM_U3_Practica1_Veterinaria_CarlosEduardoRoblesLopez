package mx.edu.ittepic.ladm_u3_practica1_veterinaria.ui.seach_pets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.Mascota
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.R
import mx.edu.ittepic.ladm_u3_practica1_veterinaria.databinding.FragmentSearchPetsBinding

class SearchPetFragment : Fragment() {

    private var _binding: FragmentSearchPetsBinding? = null

    private val binding get() = _binding!!

    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchPetsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpConsultasMascotas
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buscarMascota,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        mostrarDatosEnListView()

        binding.btnBuscar.setOnClickListener {
            var busqueda = binding.buscarMascota.text.toString()

            if (busqueda == "") {
                mostrarDatosEnListView()
            } else {
                mostrarMascotaFiltro(busqueda,binding.SpConsultasMascotas.selectedItem.toString())
            }
        }

        return  root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun mostrarDatosEnListView() {
        var listaFiltro = Mascota(requireContext()).mostrarTodos()
        var listaData = ArrayList<String>()
        var Data = Mascota(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val pets = listaFiltro.get(it)
            Data.curp = pets.id_mascota
            Data.nombre = pets.nombre
            Data.raza = pets.raza
            Data.curp = pets.curp

            listaData.add(Data.nombreMascota())
            listaIDs.add(pets.id_mascota)
        }

        binding.pets.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaData)
    }

    fun mostrarMascotaFiltro(busqueda:String, filtro:String) {
        var listaFiltro = Mascota(requireContext()).buscarFiltroMascota(busqueda,filtro)
        var listaData = ArrayList<String>()
        var Data = Mascota(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val pets = listaFiltro.get(it)
            Data.curp = pets.id_mascota
            Data.nombre = pets.nombre
            Data.raza = pets.raza
            Data.curp = pets.curp

            listaData.add(Data.nombreMascota())
            listaIDs.add(pets.id_mascota)
        }

        binding.pets.adapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaData)
    }
}