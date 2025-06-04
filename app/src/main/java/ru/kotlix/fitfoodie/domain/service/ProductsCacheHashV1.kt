package ru.kotlix.fitfoodie.domain.service

import ru.kotlix.fitfoodie.domain.dto.Product
import java.security.MessageDigest

class ProductsCacheHashV1 : ProductsCacheHash {

    override fun hash(items: List<Product>): String {
        val digest = MessageDigest.getInstance("SHA-256")

        val sortedList = items.sortedBy { it.id }

        val combinedString = buildString {
            for (product in sortedList) {
                append(product.id)
                append("|")
                append(product.title)
                append("|")
                append(product.quant)
                append("|")
                append(product.unit)
                append("|")
                val sortedTags = product.tags.map { it.name }.sorted()
                append(sortedTags.joinToString(separator = ","))
                append("\n")
            }
        }

        val hashBytes = digest.digest(combinedString.toByteArray(Charsets.UTF_8))

        return hashBytes.joinToString("") { byte ->
            "%02x".format(byte)
        }
    }
}