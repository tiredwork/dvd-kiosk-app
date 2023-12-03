package controllers

import models.Media
import persistence.Serializer
import java.util.ArrayList

/**
 * This class provides an API for managing media.
 *
 * @property serializerType The type of serializer to use for persistence.
 */
class MediaAPI(serializerType: Serializer) {

    // Private properties
    private var serializer: Serializer = serializerType
    private var medias = ArrayList<Media>()

    private var lastId = 0

    /**
     * Function to generate a unique ID for each media.
     *
     * @return A unique ID.
     */
    private fun getId() = lastId++

    /**
     * Function to format a list of media into a string.
     *
     * @param mediaToFormat The list of media to format.
     * @return A string representation of the list of media.
     */
    private fun formatListString(mediaToFormat: List<Media>): String =
        mediaToFormat
            .joinToString(separator = "\n") { media ->
                medias.indexOf(media).toString() + ": " + media.toString()
            }

    /**
     * Function to add a new media.
     *
     * @param media The media to add.
     * @return A boolean indicating whether the operation was successful.
     */
    fun add(media: Media): Boolean {
        media.mediaId = getId()
        return medias.add(media)
    }

    /**
     * Function to delete a media.
     *
     * @param id The ID of the media to delete.
     * @return A boolean indicating whether the operation was successful.
     */
    fun delete(id: Int) = medias.removeIf { media -> media.mediaId == id }

    /**
     * Function to update an existing media.
     *
     * @param id The ID of the media to update.
     * @param media The new media data.
     * @return A boolean indicating whether the operation was successful.
     */
    fun update(id: Int, media: Media?): Boolean {
        val foundMedia = findMedia(id)
        if ((foundMedia != null) && (media != null)) {
            foundMedia.mediaTitle = media.mediaTitle
            foundMedia.mediaRuntime = media.mediaRuntime
            foundMedia.mediaGenre = media.mediaGenre
            return true
        }
        return false
    }

    /**
     * Function to list all media.
     *
     * @return A string representation of all media.
     */
    fun listAllMedias() =
        if (medias.isEmpty()) {
            "No medias stored"
        } else {
            formatListString(medias)
        }

    /**
     * Function to get the number of media.
     *
     * @return The number of media.
     */
    fun numberOfMedias() = medias.size

    /**
     * Function to find a media by their ID.
     *
     * @param mediaId The ID of the media to find.
     * @return The media if found, null otherwise.
     */
    fun findMedia(mediaId: Int): Media? {
        return medias.find { media -> media.mediaId == mediaId }
    }

    /**
     * Function to search media by title.
     *
     * @param searchString The string to search for in the media titles.
     * @return A string representation of the search results.
     */
    fun searchMediasByTitle(searchString: String) =
        formatListString(
            medias.filter { media -> media.mediaTitle.contains(searchString, ignoreCase = true) }
        )

    /**
     * Function to load media from persistent storage.
     *
     * @throws Exception If an error occurs during loading.
     */
    @Throws(Exception::class)
    fun load() {
        medias = serializer.read() as ArrayList<Media>
    }

    /**
     * Function to save media to persistent storage.
     *
     * @throws Exception If an error occurs during saving.
     */
    @Throws(Exception::class)
    fun save() {
        serializer.write(medias)
    }
}
