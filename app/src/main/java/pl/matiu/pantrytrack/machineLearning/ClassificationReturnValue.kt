package pl.matiu.pantrytrack.machineLearning

data class ClassificationReturnValue(
    val productName: String,
    val classificationValue: Float,
    val productId: Int
)
