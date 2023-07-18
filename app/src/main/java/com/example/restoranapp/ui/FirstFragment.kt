package com.example.restoranapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restoranapp.R
import com.example.restoranapp.application.RestoApp
import com.example.restoranapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val restoViewModel: RestoViewModel by viewModels {
        RestoViewModelFactory((applicationContext as RestoApp).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RestoListAdapter{ resto ->
            //ini list yang bisa di klik dan mendapatkan data resto jadi tidak null
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(resto)
            findNavController().navigate(action)
        }
            binding.datarecyclerView.adapter = adapter
            binding.datarecyclerView.layoutManager = LinearLayoutManager(context)
            restoViewModel.allRestos.observe(viewLifecycleOwner) { restos ->
            restos.let {
                if (restos.isEmpty()){
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.imageView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                }
                adapter.submitList(restos)
                }
            }


            binding.addFAB.setOnClickListener {
                //ini button tambah jadi resto pasti null
               val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
                findNavController().navigate(action)
            }
            binding.phoneButton.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_thirdFragment)
            }
            binding.collectionButton.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_fourthFragment)
            }
            binding.biografiButton.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_fifthFragment)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}