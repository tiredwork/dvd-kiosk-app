package models

import utils.Utilities

data class Media(var mediaId: Int = 0,
                var mediaTitle: String,
                var mediaPriority: Int,
                var mediaCategory: String,
                var isMediaArchived: Boolean = false)
{
    private var lastItemId = 0
    private fun getItemId() = lastItemId++

    override fun toString(): String {
        val archived = if (isMediaArchived) 'Y' else 'N'
        return "$mediaId: $mediaTitle, Priority($mediaPriority), Category($mediaCategory), Archived($archived)"
    }
}