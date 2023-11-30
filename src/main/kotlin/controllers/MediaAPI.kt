package controllers

import models.Media
import utils.Utilities.formatListString
import java.util.ArrayList

class MediaAPI() {

    private var medias = ArrayList<Media>()

    private var lastId = 0
    private fun getId() = lastId++


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
            foundMedia.mediaPriority = media.mediaPriority
            foundMedia.mediaCategory = media.mediaCategory
            return true
        }

        // if the media was not found, return false, indicating that the update was not successful
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR MEDIA ArrayList
    // ----------------------------------------------
    fun listAllMedias() =
        if (medias.isEmpty()) "No medias stored"
        else formatListString(medias)

    // ----------------------------------------------
    //  COUNTING METHODS FOR MEDIA ArrayList
    // ----------------------------------------------
    fun numberOfMedias() = medias.size

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findMedia(mediaId : Int) =  medias.find{ media -> media.mediaId == mediaId }

    fun searchMediasByTitle(searchString: String) =
       formatListString(
            medias.filter { media -> media.mediaTitle.contains(searchString, ignoreCase = true) }
        )

    }
