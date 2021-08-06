package com.kabirland.absensidigital_demo.ui.operator.belumAbsen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.model.ModelUser
import com.kabirland.absensidigital_demo.utils.Constant
import com.kabirland.absensidigital_demo.utils.onClickFoto
import kotlinx.android.synthetic.main.item_belum_absen.view.*

class AdapterBelumAbsen(
    private val listAfiliasi: ArrayList<ModelUser>,
    private val onClik: (ModelUser, String) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<AdapterBelumAbsen.AfiliasiHolder>() {
    lateinit var idHari : String

    inner class AfiliasiHolder(private val itemV: View) :
        RecyclerView.ViewHolder(itemV) {
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(itemData: ModelUser) {
            itemV.textNama.text = itemData.nama
            itemV.textNip.text = itemData.username
            itemV.textJabatan.text = Constant.pegawai

            itemV.imgFoto.load(itemData.foto) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            itemV.imgFoto.setOnClickListener {
                onClickFoto(itemData.foto,
                    navController)
            }

            itemV.btnKonfirmasi.setOnClickListener {
                onClik(itemData, idHari)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_belum_absen,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listAfiliasi[position])
    }
}
