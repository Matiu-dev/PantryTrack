package pl.matiu.pantrytrack.product

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity

class ScannedProductAdapter(private var products: List<ProductScannedEntity>?,
                            private val onItemClick: (ProductScannedEntity) -> Unit,
                            private val deleteItemClick: (ProductScannedEntity) -> Unit) : RecyclerView.Adapter<ScannedProductAdapter.ViewHolder>() {

    fun updateProducts(newProducts: List<ProductScannedEntity>) {
        this.products = newProducts
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameButton: Button = view.findViewById(R.id.product_name)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_product_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.scanned_product_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = products?.size ?: 0

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.productNameButton.text = "Name: ${products?.get(position)?.categoryName}, Amount: ${products?.get(position)?.amount}"
        val product = products?.get(position) ?: return
        viewHolder.productNameButton.setOnClickListener {
            onItemClick(product)
        }

        viewHolder.deleteButton.setOnClickListener {
            deleteItemClick(product)
        }
    }
}