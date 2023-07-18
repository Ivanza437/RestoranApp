package com.example.restoranapp.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.restoranapp.R
import com.example.restoranapp.application.RestoApp
import com.example.restoranapp.databinding.FragmentSecondBinding
import com.example.restoranapp.model.Resto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val restoViewModel: RestoViewModel by viewModels {
        RestoViewModelFactory((applicationContext as RestoApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var resto: Resto? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        // binding google map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()


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
                    // kita kasih default dulu
                    val resto = Resto(0, name.toString(), kind.toString(), price.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    restoViewModel.insert(resto)
                } else {
                    val resto = Resto(resto?.id!!, name.toString(), kind.toString(), price.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //implement drag marker

        //untuk zoom in dan zoom out di map
        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerDragListener(this)

    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onMarkerDragStart(p0: Marker) {

    }
    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            )   == PackageManager.PERMISSION_GRANTED
        ){
            getCurrentLocation()
        } else {
            Toast.makeText(applicationContext, "Akses Lokasi Ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation(){
        //mengecek jika permission tidak distujui maka akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )   != PackageManager.PERMISSION_GRANTED
        ){
            return
        }

        //untuk test current location coba run di device langsung atau build apk trus instal di hp masing masing
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
            if (location != null){
                var latLang = LatLng(location.latitude, location.longitude)
                currentLatLang = latLang
                var title = "Marker"
                //menampilkan lokasi sesuai koordinat yang sudah disimpan/diupdate tadi

                if (resto != null ){
                    title = resto?.name.toString()
                    val newCurrentLocation = LatLng(resto?.latitude!!, resto?.longitude!!)
                    latLang = newCurrentLocation
                }
                val markerOption = MarkerOptions()
                    .position(latLang)
                    .title(title)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_res_taurant))
                mMap.addMarker(markerOption)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15f))
            }
            }
    }
}