package com.example.restoranapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.restoranapp.R
import com.example.restoranapp.application.RestoApp
import com.example.restoranapp.databinding.FragmentSecondBinding
import com.example.restoranapp.model.Resto

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val restoViewModel: RestoViewModel by viewModels {
        RestoViewModelFactory((applicationContext as RestoApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var resto: Resto? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resto = args.resto

        if(resto != null ){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.nameEditText.setText(resto?.name)
            binding.kindEditText.setText(resto?.kind)
            binding.priceEditText.setText(resto?.price)
        }
        val name = binding.nameEditText.text
        val kind = binding.kindEditText.text
        val price = binding.priceEditText.text
        binding.saveButton.setOnClickListener {
            //kondisi jika inputan kosong maka data tidak dapat tampil/menyimpan
            if (name.isEmpty()){
                Toast.makeText(context, "Nama Menu Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else if (kind.isEmpty()){
                Toast.makeText(context, "Jenis Menu Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else if (price.isEmpty()){
                Toast.makeText(context, "Harga Menu Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }   else {
                if (resto == null ) {
                    val resto = Resto(0, name.toString(), kind.toString(), price.toString())
                    restoViewModel.insert(resto)
                } else {
                    val resto = Resto(resto?.id!!, name.toString(), kind.toString(), price.toString())
                    restoViewModel.update(resto)
                }
                findNavController().popBackStack() //untuk dismiss halaman ini
            }
        }

        binding.deleteButton.setOnClickListener {
            resto?.let { restoViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}