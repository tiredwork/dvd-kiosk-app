package models

data class Customer(
    var customerId: Int,
    var fName: String,
    var lName: String,
    var email: String,
    var phoneNo: String,
    var postCode: String,
) {
    private val customers = mutableListOf<Customer>()
    private var lastCustomerId = 0
    private fun getNextCustomerId() = lastCustomerId++

    private var rentedMediaList = mutableListOf<Media>()
    override fun toString(): String {
        return "$customerId: Name: $fName $lName, Email: $email, Phone: $phoneNo, Postcode: $postCode"
    }

    fun addRentals(media: Media) {
        rentedMediaList.add(media)
    }

    fun getRentals(): List<Media> {
        return rentedMediaList.toList()
    }

    fun updateCustomer(updatedCustomer: Customer): Boolean {
        if (customerId == updatedCustomer.customerId) {
            fName = updatedCustomer.fName
            lName = updatedCustomer.lName
            email = updatedCustomer.email
            phoneNo = updatedCustomer.phoneNo
            postCode = updatedCustomer.postCode
            return true
        }
        return false
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
        if (rentedMediaList.contains(media)) {
            rentedMediaList.remove(media)
            println("Media returned successfully.")
        } else {
            println("This media item is not in the customer's rented list.")
        }
    }

    fun joinToString(separator: String, param: (Any) -> String): String {
        return "Customer ID: $customerId, First Name: $fName, Last Name: $lName, Email: $email, Phone Number: $phoneNo, Postcode: $postCode"
    }

}
