package com.bangkep.sistemabsensi.ui.pegawai.riwayat.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.databinding.FragmentDetailRiwayatBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailRiwayatFragment : BaseFragmentBind<FragmentDetailRiwayatBinding>(), OnMapReadyCallback {
    private lateinit var viewModel: DetailRiwayatViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_detail_riwayat
    private val mapBundelKey = "MapViewBundleKey"
    private var gmap: GoogleMap? = null
    private var marker: Marker? = null

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailRiwayatViewModel(findNavController(), savedData)
        bind.viewModel = viewModel

        setData()
        setMap(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        viewModel.dataUser.value = savedData.getDataUser()
        viewModel.dataAbsensi.value = this.arguments?.getParcelable(Constant.reffAbsensi)
        viewModel.dataNipNama.value = "${savedData.getDataUser()?.username} - ${savedData.getDataUser()?.nama}"
        viewModel.jenisAbsen.value = "Absen : ${viewModel.dataAbsensi.value?.jenis}"

        when (viewModel.dataAbsensi.value?.status) {
            "1" -> {
                bind.textStatusAbsen.text = "Status : Dikonfirmasi"
                bind.textStatusAbsen.setTextColor(Color.GREEN)
            }
            "2" -> {
                bind.textStatusAbsen.text = "Status : Ditolak"
                bind.textStatusAbsen.setTextColor(Color.RED)
            }
            else -> {
                bind.textStatusAbsen.text = "Status : Belum dikonfirmasi"
                bind.textStatusAbsen.setTextColor(Color.BLUE)
            }
        }
    }

    private fun setMap(savedInstanceState: Bundle?) {
        viewModel.isShowLoading.value = true
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(mapBundelKey)
        }
        bind.mapView.onCreate(mapViewBundle)
        bind.mapView.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(mapBundelKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(mapBundelKey, mapViewBundle)
        }
        bind.mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        bind.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        bind.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        bind.mapView.onStop()
    }

    override fun onPause() {
        bind.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        bind.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        bind.mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap?.setMinZoomPreference(15f)
        viewModel.isShowLoading.value = false

        val latitude = viewModel.dataAbsensi.value?.latitude
        val longitude = viewModel.dataAbsensi.value?.longitude

        if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty()){
            val lat = latitude.toDouble()
            val longit = longitude.toDouble()
            val myLocation = LatLng(lat, longit)
            gmap?.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            gmap?.setMinZoomPreference(15f)
            val place = MarkerOptions().position(myLocation).title("Lokasi Absen")
            marker = gmap?.addMarker(place)
        }
        else{
            viewModel.message.value = "Error, gagal mengambil lokasi absen"
        }
    }
}

