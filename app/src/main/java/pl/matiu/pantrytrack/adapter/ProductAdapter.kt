package pl.matiu.pantrytrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.matiu.pantrytrack.model.Product
import pl.matiu.pantrytrack.R

class ProductAdapter(private var products: List<Product>?) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        this.products = newProducts
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.product_name)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = products?.size ?: 0

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.productName.text = "Name: ${products?.get(position)?.name}, amount: ${products?.get(position)?.amount}, price: ${products?.get(position)?.price}"
    }
}