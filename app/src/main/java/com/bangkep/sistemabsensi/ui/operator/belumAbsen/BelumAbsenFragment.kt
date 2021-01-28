package com.bangkep.sistemabsensi.ui.operator.belumAbsen

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentBelumAbsenBinding
import com.bangkep.sistemabsensi.utils.Constant

class BelumAbsenFragment : BaseFragmentBind<FragmentBelumAbsenBinding>() {
    private lateinit var viewModel: BelumAbsenViewModel
    private lateinit var searchView : SearchView
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun getLayoutResource(): Int = R.layout.fragment_belum_absen

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BelumAbsenViewModel(bind.rvData, context, savedData, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()

        viewModel.getHariKerja()

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.getHariKerja()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

        searchView.queryHint = "Cari Nama"

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.listData.clear()
                viewModel.adapter?.notifyDataSetChanged()

                for (i in viewModel.listNama.indices){
                    if (viewModel.listNama[i].nama.contains(query)){
                        viewModel.listData.add(viewModel.listNama[i])
                        viewModel.adapter?.notifyDataSetChanged()
                    }
                }

                if (viewModel.listData.size == 0){
                    viewModel.message.value = Constant.noData
                }
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

