package utils

import models.Customer
import models.Media

/**
 * This object provides utility functions for formatting lists and sets.
 */
object Utilities {

    // MEDIA: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    // name; we don't have to create an object of Utilities to use them.

    /**
     * Function to format a list of media into a string.
     *
     * @param mediasToFormat The list of media to format.
     * @return A string representation of the list of media.
     */
    @JvmStatic
    fun formatListString(mediasToFormat: List<Media>): String =
        mediasToFormat
            .joinToString(separator = "\n") { media -> "$media" }

    /**
     * Function to format a set of items into a string.
     *
     * @param itemsToFormat The set of items to format.
     * @return A string representation of the set of items.
     */
    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Customer>): String =
        itemsToFormat
            .joinToString(separator = "\n") { item -> "\t$item" }
}
