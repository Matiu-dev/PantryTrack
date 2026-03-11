package pl.matiu.pantrytrack.fragments.scannerFragment

import pl.matiu.pantrytrack.productDatabase.productDetails.Type

data class DialogFragmentModel(
    val productDetailsId: Int,
    val type: Type,
    val productName: String,
    val targetQuantity: Int
)
