package models

import utils.Utilities

data class Media(var mediaId: Int = 0,
                 var mediaTitle: String,
                 var mediaRuntime: Int,
                 var mediaGenre: String,
                 var isRented: Boolean)
{

    private var lastMediaId = 0
    private fun getNextMediaId() = lastMediaId++

    override fun toString(): String {
        return "$mediaId: $mediaTitle, Runtime: $mediaRuntime, Category: $mediaGenre, Is it Rented?: $isRented"
    }

    fun formatListString(list: List<Any>): String {
        return list.joinToString("\n")
    }

}