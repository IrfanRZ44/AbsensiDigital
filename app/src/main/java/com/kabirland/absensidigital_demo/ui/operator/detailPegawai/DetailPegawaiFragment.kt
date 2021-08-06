package com.kabirland.absensidigital_demo.ui.operator.detailPegawai

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind
import com.kabirland.absensidigital_demo.databinding.FragmentDetailPegawaiBinding
import com.kabirland.absensidigital_demo.utils.Constant

class DetailPegawaiFragment : BaseFragmentBind<FragmentDetailPegawaiBinding>() {
    private lateinit var viewModel: DetailPegawaiViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_detail_pegawai

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailPegawaiViewModel(findNavController(), activity, savedData)
        bind.viewModel = viewModel

        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        viewModel.dataUser.value = this.arguments?.getParcelable(Constant.reffPegawai)
        viewModel.idHari.value = this.arguments?.getString(Constant.reffIdHari)
        viewModel.dataNipNama.value = "${viewModel.dataUser.value?.username} - ${viewModel.dataUser.value?.nama}"
        viewModel.jenisAbsen.value = Constant.pegawai
    }
}

