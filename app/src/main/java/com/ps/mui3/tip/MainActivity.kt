package com.ps.mui3.tip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ps.mui3.tip.icons.IconInfo
import com.ps.mui3.tip.icons.IconsHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val allIcons = IconsHelper.getIconsList(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = RVAdapter(this, allIcons)
    }

    class RVAdapter(
        private val context: Context,
        private val icons: List<IconInfo>
    ) : RecyclerView.Adapter<RVAdapter.RVViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
            return RVViewHolder(LayoutInflater.from(context).inflate(R.layout.tile, null, false))
        }

        override fun getItemCount() = icons.size

        override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
            val icon = icons[position]

            holder.itemView.setOnClickListener {
                buildDialog(context, icon).show()
            }

            holder.iconLabel.text = icon.label
            loadIcon(holder.iconView, icon.drawableResId)
        }

        private fun buildDialog(context: Context, iconInfo: IconInfo): AlertDialog {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_icon_info, null)

            val iconView: ImageView = dialogView.findViewById(R.id.icon)
            val iconLabel: TextView = dialogView.findViewById(R.id.icon_label)
            val iconDrawableName: TextView = dialogView.findViewById(R.id.icon_drawable_name)
            val iconPackageName: TextView = dialogView.findViewById(R.id.icon_package_name)
            val iconClassName: TextView = dialogView.findViewById(R.id.icon_class_name)

            loadIcon(iconView, iconInfo.drawableResId)
            iconLabel.text = iconInfo.label
            iconDrawableName.text = iconInfo.drawableName
            iconPackageName.text = iconInfo.packageName
            iconClassName.text = iconInfo.className

            return MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .create()
        }

        private fun loadIcon(imageView: ImageView, drawableRes: Int) {
            Glide.with(context)
                .load(drawableRes)
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
        }

        class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val iconView: ImageView = itemView.findViewById(R.id.tile_icon)
            val iconLabel: TextView = itemView.findViewById(R.id.tile_label)
        }

    }

}