package org.jane.gtelinternship.product.domain.model

sealed interface Product {
    val sku: String
    val name: String
    val description: String
}

data class LogicomProduct(
    override val sku: String,
    override val name: String,
    override val description: String,
    val brand: String,
    val category: String,
    val image: List<String>
) : Product


data class WooProduct(
    val id: Long,
    override val sku: String,
    override val name: String,
    override val description: String,
) : Product

