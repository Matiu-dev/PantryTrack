package pl.matiu.pantrytrack.fragments.categoryFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.api.ApiRepository
import pl.matiu.pantrytrack.productDatabase.category.CategoryEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.Type
import pl.matiu.pantrytrack.sharedPrefs.SharedPrefs
import javax.inject.Inject
import kotlin.math.PI

class CategoryAdapter(private var categories: List<CategoryEntity>?,
                      private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    fun updateCategories(newCategories: List<CategoryEntity>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.category_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = categories?.size ?: 0

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.categoryName.text = "Kategoria: ${categories?.get(position)?.categoryName}"

        viewHolder.categoryName.setOnClickListener {
            //jesli istnieje taki model
            categories?.get(position)?.categoryName?.let { modelName ->
                onCategoryClick(modelName)
            }

//            SharedPrefs().saveType()
//            categories?.get(position)?.categoryName
//            when(categories?.get(position)?.categoryName) {
//                Type.DAIRY.toString() -> navController.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.DAIRY.toString()))
//                else -> Toast.makeText(context, "Nie ma modelu dla tej kategorii", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}