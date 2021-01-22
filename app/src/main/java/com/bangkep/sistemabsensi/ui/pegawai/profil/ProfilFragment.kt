package com.bangkep.sistemabsensi.ui.pegawai.profil

import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentProfilBinding

class ProfilFragment : BaseFragmentBind<FragmentProfilBinding>() {
    private lateinit var viewModel: ProfilViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_profil

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = ProfilViewModel()
        bind.viewModel = viewModel
    }

}

