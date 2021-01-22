package com.bangkep.sistemabsensi.ui.pegawai.riwayat

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentRiwayatBinding

class RiwayatFragment : BaseFragmentBind<FragmentRiwayatBinding>() {
    private lateinit var viewModel: RiwayatViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_riwayat

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = RiwayatViewModel()
        bind.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_pesan_and_kontak, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionPesan ->{
                viewModel.message.value = "Pesan"
            }
            R.id.actionKontak->{
                viewModel.message.value = "Kontak"
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

