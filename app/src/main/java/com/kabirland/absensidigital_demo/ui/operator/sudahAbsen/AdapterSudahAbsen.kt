package com.kabirland.absensidigital_demo.ui.operator.sudahAbsen

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.model.ModelSudahAbsensi
import com.kabirland.absensidigital_demo.utils.onClickFoto
import kotlinx.android.synthetic.main.item_sudah_absen.view.*

class AdapterSudahAbsen(
    private val listAfiliasi: ArrayList<ModelSudahAbsensi>,
    private val onClik: (ModelSudahAbsensi) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<AdapterSudahAbsen.AfiliasiHolder>() {

    inner class AfiliasiHolder(private val itemV: View) :
        RecyclerView.ViewHolder(itemV) {
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(itemData: ModelSudahAbsensi) {
            itemV.textNama.text = itemData.nama
            itemV.textNip.text = itemData.nip
            itemV.textTanggal.text = itemData.date_created
            itemV.textJenis.text = "Absen : ${itemData.jenis}"

            when (itemData.status) {
                "1" -> {
                    itemV.btnKonfirmasi.visibility = View.GONE
                    itemV.textStatus.text = "Status : Dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.GREEN)
                }
                "2" -> {
                    itemV.btnKonfirmasi.visibility = View.GONE
                    itemV.textStatus.text = "Status : Ditolak"
                    itemV.textStatus.setTextColor(Color.RED)
                }
                else -> {
                    itemV.btnKonfirmasi.visibility = View.VISIBLE
                    itemV.textStatus.text = "Status : Belum dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.BLUE)
                }
            }
            itemV.imgFoto.load(itemData.img) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            itemV.imgFoto.setOnClickListener {
                onClickFoto(itemData.img,
                    navController)
            }

            itemV.btnKonfirmasi.setOnClickListener {
                onClik(itemData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_sudah_absen,
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
