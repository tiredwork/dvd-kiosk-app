package models

/**
 * This data class represents a Media.
 *
 * @property mediaId The unique ID of the media.
 * @property mediaTitle The title of the media.
 * @property mediaRuntime The runtime of the media.
 * @property mediaGenre The genre of the media.
 * @property isRented A boolean indicating whether the media is rented.
 */
data class Media(
    var mediaId: Int,
    var mediaTitle: String,
    var mediaRuntime: String,
    var mediaGenre: String,
    var isRented: Boolean
) {

    /**
     * Function to create a string representation of the Media object.
     *
     * @return A string representation of the Media object.
     */
    override fun toString(): String {
        return "Media ID: $mediaId: '$mediaTitle', Runtime: '$mediaRuntime', Genre: '$mediaGenre', Is it Rented?: $isRented"
    }

    /**
     * Function to format a list of items into a string.
     *
     * @param list The list of items to format.
     * @return A string representation of the list.
     */
    fun formatListString(list: List<Any>): String {
        return list.joinToString("\n")
    }
}
