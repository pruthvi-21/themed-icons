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
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.ps.mui3.tip.icons.IconInfo
import com.ps.mui3.tip.icons.IconsHelper

class MainActivity : AppCompatActivity() {

    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView
    private lateinit var noSearchResultsView: TextView
    private lateinit var searchResultCount: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBar = findViewById(R.id.search_bar)
        searchView = findViewById(R.id.search_view)
        noSearchResultsView = findViewById(R.id.no_search_results)
        searchResultCount = findViewById(R.id.search_result_count)
        recyclerView = findViewById(R.id.recycler_view)
        searchRecyclerView = findViewById(R.id.search_recycler_view)

        val allIcons = IconsHelper.getIconsList(this)
        val allIconsAdapter = RVAdapter(this, allIcons)
        val searchIconsAdapter = RVAdapter(this, allIcons)

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = allIconsAdapter

        searchRecyclerView.layoutManager = GridLayoutManager(this, 4)
        searchRecyclerView.adapter = searchIconsAdapter
        searchIconsAdapter.updateList("")

        searchBar.hint = getString(R.string.search_bar_hint, allIcons.size)

        searchView.setupWithSearchBar(searchBar)
        searchView.editText.doOnTextChanged { text, _, _, _ ->
            val filteredCount = searchIconsAdapter.updateList(text.toString())
            noSearchResultsView.isVisible = filteredCount == 0
            searchResultCount.text = getString(R.string.search_result_count, filteredCount)
        }
    }

    class RVAdapter(
        private val context: Context,
        private val icons: List<IconInfo>
    ) : RecyclerView.Adapter<RVAdapter.RVViewHolder>() {

        private var layoutInflater = LayoutInflater.from(context)
        private var filteredIcons: MutableList<IconInfo> = icons.toMutableList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
            return RVViewHolder(layoutInflater.inflate(R.layout.tile, parent, false))
        }

        override fun getItemCount() = filteredIcons.size

        override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
            val icon = filteredIcons[position]

            holder.itemView.setOnClickListener {
                buildDialog(context, icon).show()
            }

            holder.iconLabel.text = icon.label
            loadIcon(holder.iconView, icon.drawableResId)
        }

        private fun buildDialog(context: Context, iconInfo: IconInfo): AlertDialog {
            val dialogView = layoutInflater.inflate(R.layout.dialog_icon_info, null)

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
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
        }

        fun updateList(filter: String): Int {
            filteredIcons.clear()

            if (filter.isNotEmpty()) {
                icons.forEach {
                    if (it.label.contains(filter.trim(), true))
                        filteredIcons.add(it)
                }
            }

            notifyDataSetChanged()
            return filteredIcons.size
        }

        class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val iconView: ImageView = itemView.findViewById(R.id.tile_icon)
            val iconLabel: TextView = itemView.findViewById(R.id.tile_label)
        }

    }

}