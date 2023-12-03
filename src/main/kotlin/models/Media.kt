package models

data class Media(
    var mediaId: Int,
    var mediaTitle: String,
    var mediaRuntime: String,
    var mediaGenre: String,
    var isRented: Boolean
) {

    override fun toString(): String {
        return "Media ID: $mediaId: '$mediaTitle', Runtime: '$mediaRuntime', Category: '$mediaGenre', Is it Rented?: $isRented"
    }

    fun formatListString(list: List<Any>): String {
        return list.joinToString("\n")
    }
}
