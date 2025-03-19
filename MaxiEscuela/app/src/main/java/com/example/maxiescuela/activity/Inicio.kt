package com.example.maxiescuela.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.FragmentInicioBinding
import com.denzcoskun.imageslider.models.SlideModel

class Inicio : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = arrayListOf(
            SlideModel(R.drawable.imagen1, "Salón 1", ScaleTypes.FIT),
            SlideModel(R.drawable.imagen2, "Salón 2", ScaleTypes.FIT),
            SlideModel(R.drawable.imagen3, "Salón 3", ScaleTypes.FIT),
            SlideModel(R.drawable.imagen4, "Salón 4", ScaleTypes.FIT)
        )

        // Verificar si el binding está disponible antes de usarlo
        binding.imageSlider.setImageList(imageList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}
