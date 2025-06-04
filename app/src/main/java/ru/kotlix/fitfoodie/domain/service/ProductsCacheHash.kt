package ru.kotlix.fitfoodie.domain.service

import ru.kotlix.fitfoodie.domain.dto.Product

interface ProductsCacheHash {
    fun hash(items: List<Product>): String
}