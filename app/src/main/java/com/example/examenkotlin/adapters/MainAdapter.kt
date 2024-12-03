package com.example.examenkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.examenkotlin.data.PeliculaClass
import com.example.examenkotlin.data.PeliculasResponse
import com.example.examenkotlin.databinding.ItemPeliculaBinding
import com.squareup.picasso.Picasso

class MainAdapter (var items: List<PeliculaClass>, val onItemClick:(Int) -> Unit) : RecyclerView.Adapter<Main_ViewHolder>() {
    override fun onBindViewHolder(holder: Main_ViewHolder, position: Int) {
        val pelicula_selected = items[position]
        holder.render(pelicula_selected)
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Main_ViewHolder {
        val binding = ItemPeliculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Main_ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<PeliculaClass>) {
        this.items = items
        notifyDataSetChanged()
    }
}

// -----------------------------

class Main_ViewHolder(val binding: ItemPeliculaBinding) : RecyclerView.ViewHolder(binding.root) {
    fun render(pelicula: PeliculaClass) {
        binding.peliculaTextView.text = "   " + pelicula.title
        Picasso.get().load(pelicula.poster).into(binding.peliculaThumbnail)

    }
}
