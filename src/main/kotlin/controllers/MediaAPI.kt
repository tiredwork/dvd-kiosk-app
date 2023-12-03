package controllers

import models.Media
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

class MediaAPI(serializerType: Serializer) {

    // Private properties
    private var serializer: Serializer = serializerType
    private var medias = ArrayList<Media>()

    private var lastId = 0
    private fun getId() = lastId++

    private fun formatListString(mediaToFormat: List<Media>): String =
        mediaToFormat
            .joinToString(separator = "\n") { media ->
                medias.indexOf(media).toString() + ": " + media.toString()
            }

    fun add(media: Media): Boolean {
        media.mediaId = getId()
        return medias.add(media)
    }

    fun delete(id: Int) = medias.removeIf { media -> media.mediaId == id }

    fun update(id: Int, media: Media?): Boolean {
        // find the media object by the index number
        val foundMedia = findMedia(id)

        // if the media exists, use the media details passed as parameters to update the found media in the ArrayList.
        if ((foundMedia != null) && (media != null)) {
            foundMedia.mediaTitle = media.mediaTitle
            foundMedia.mediaRuntime = media.mediaRuntime
            foundMedia.mediaGenre = media.mediaGenre
            return true
        }

        // if the media was not found, return false, indicating that the update was not successful
        return false
    }

    fun listAllMedias() =
        if (medias.isEmpty()) {
            "No medias stored"
        } else {
            formatListString(medias)
        }

    fun numberOfMedias() = medias.size

    fun findMedia(mediaId: Int): Media? {
        return medias.find { media -> media.mediaId == mediaId }
    }

    fun searchMediasByTitle(searchString: String) =
        formatListString(
            medias.filter { media -> media.mediaTitle.contains(searchString, ignoreCase = true) }
        )

    @Throws(Exception::class)
    fun load() {
        medias = serializer.read() as ArrayList<Media>
    }

    @Throws(Exception::class)
    fun save() {
        serializer.write(medias)
    }
}
