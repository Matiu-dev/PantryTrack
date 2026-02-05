package pl.matiu.pantrytrack.productDatabase

import pl.matiu.pantrytrack.fragments.product.Product

fun toProductEntity(product: Product): ProductEntity {
    return ProductEntity(
        name = product.name,
        amount = product.amount,
        price = product.price
    )
}

fun toProduct(productEntity: ProductEntity): Product {
    return Product(
        name = productEntity.name,
        amount = productEntity.amount,
        price = productEntity.price
    )
}