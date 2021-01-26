package com.bangkep.sistemabsensi.ui.pegawai.riwayat

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentRiwayatBinding

class RiwayatFragment : BaseFragmentBind<FragmentRiwayatBinding>() {
    private lateinit var viewModel: RiwayatViewModel
    private lateinit var searchView : SearchView
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun getLayoutResource(): Int = R.layout.fragment_riwayat

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = RiwayatViewModel(bind.rvData, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()

        val nip = savedData.getDataUser()?.username

        if (!nip.isNullOrEmpty()){
            viewModel.getListRiwayat(nip)
        }
        else{
            viewModel.message.value = "Error, terjadi kesalahan database, mohon login ulang"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

        searchView.queryHint = "Cari Tanggal"

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.listData.clear()
                viewModel.adapter?.notifyDataSetChanged()

                for (i in viewModel.listNama.indices){
                    if (viewModel.listNama[i].date_created.contains(query)){
                        viewModel.listData.add(viewModel.listNama[i])
                        viewModel.adapter?.notifyDataSetChanged()
                    }
                }

                viewModel.cekList()
                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.listData.clear()
            viewModel.adapter?.notifyDataSetChanged()

            for (i in viewModel.listDataSearch.indices){
                viewModel.listData.add(viewModel.listDataSearch[i])
                viewModel.adapter?.notifyDataSetChanged()
            }
            viewModel.cekList()
            false
        }

        searchView.setOnQueryTextListener(queryTextListener)
        searchView.setOnCloseListener(onCloseListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSearch ->{
                return false
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)
        searchView.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }
}

