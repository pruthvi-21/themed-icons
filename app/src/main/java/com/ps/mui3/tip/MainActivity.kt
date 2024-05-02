package com.ps.mui3.tip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = FlexboxLayoutManager(this).apply {
            justifyContent = JustifyContent.SPACE_EVENLY
        }
        recyclerView.adapter = RVAdapter(this, PreviewItems.ICONS)
    }

    class RVAdapter(
        private val context: Context,
        private val list: Array<Int>
    ) : RecyclerView.Adapter<RVAdapter.RVViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
            return RVViewHolder(LayoutInflater.from(context).inflate(R.layout.tile, null, false))
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
            Glide.with(context)
                .load(list[position])
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.iconView)
        }

        class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val iconView: ImageView = itemView.findViewById(R.id.tile_icon)
        }

    }

}