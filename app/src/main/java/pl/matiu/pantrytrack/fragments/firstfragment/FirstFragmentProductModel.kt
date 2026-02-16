package pl.matiu.pantrytrack.fragments.firstfragment

data class FirstFragmentProductModel(
    val productName: String,
    val amount: Int,
    var scannedPhoto: ByteArray,
    val productDetailsId: Int,
    val productId: Int
)
