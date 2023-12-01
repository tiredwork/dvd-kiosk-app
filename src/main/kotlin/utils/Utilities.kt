package utils

import models.Customer
import models.Media

object Utilities {

    // MEDIA: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(mediasToFormat: List<Media>): String =
        mediasToFormat
            .joinToString(separator = "\n") { media ->  "$media" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Customer>): String =
        itemsToFormat
            .joinToString(separator = "\n") { item ->  "\t$item" }

}
