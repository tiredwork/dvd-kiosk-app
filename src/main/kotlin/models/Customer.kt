package models

import controllers.MediaAPI
import persistence.Serializer

data class Customer(
    var customerId: Int,
    var fName: String,
    var lName: String,
    var email: String,
    var phoneNo: String,
    var postCode: String,
    var rentedMedia: MutableList<Int> = mutableListOf()
) {

class CustomerAPI(private val mediaAPI: MediaAPI, serializerType: Serializer) {

    private val customers = mutableListOf<Customer>()
    private var lastCustomerId = 0
    private fun getNextCustomerId() = lastCustomerId++

    private var rentedMediaList = mutableListOf<Media>()

    fun rentMedia(customerId: Int, mediaId: Int): Boolean {
        val customer = customers.find { it.customerId == customerId }
        val media = mediaAPI.findMedia(mediaId)

        return if (customer != null && media != null && !media.isRented) {
            customer.rentedMedia.add(mediaId)
            media.isRented = true
            println("Media rented successfully")
            true
        } else {
            println("Failed to rent media")
            false
        }
    }

    fun returnMedia(customerId: Int, mediaId: Int): Boolean {
        val customer = customers.find { it.customerId == customerId }
        val media = mediaAPI.findMedia(mediaId)

        return if (customer != null && media != null && media.isRented) {
            customer.rentedMedia.remove(mediaId)
            media.isRented = false
            println("Media returned successfully")
            true
        } else {
            println("Failed to return media")
            false
        }
    }

    fun getRentedMedia(customerId: Int): List<Media> {
        val customer = customers.find { it.customerId == customerId }
        return customer?.rentedMedia?.mapNotNull { mediaId -> mediaAPI.findMedia(mediaId) } ?: emptyList()
    }

    fun addRentals(media: Media) {
        rentedMediaList.add(media)
    }

    fun getRentals(): List<Media> {
        return rentedMediaList.toList()
    }

    fun updateCustomer(updatedCustomer: Customer): Boolean {
        val customer = customers.find { it.customerId == updatedCustomer.customerId }
        return if (customer != null) {
            customer.fName = updatedCustomer.fName
            customer.lName = updatedCustomer.lName
            customer.email = updatedCustomer.email
            customer.phoneNo = updatedCustomer.phoneNo
            customer.postCode = updatedCustomer.postCode
            true
        } else {
            false
        }
    }

    fun listCustomers() {
        customers.forEach { customer ->
            println(customer.toString())
        }
    }

    fun deleteRentals(mediaId: Int): Boolean {
        val mediaToRemove = rentedMediaList.find { media -> media.mediaId == mediaId }
        return mediaToRemove?.let { media -> rentedMediaList.remove(media) } ?: false
    }

    fun returnRentedMedia(media: Media) {
        val customer = customers.find { it.rentedMedia.contains(media.mediaId) }
        if (customer != null && media.isRented) {
            customer.rentedMedia.remove(media.mediaId)
            media.isRented = false
            println("Media returned successfully.")
        } else {
            println("This media item is not in the customer's rented list or it was not rented.")
        }
    }
}}
