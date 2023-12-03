package models

/**
 * This data class represents a Customer.
 *
 * @property customerId The unique ID of the customer.
 * @property fName The first name of the customer.
 * @property lName The last name of the customer.
 * @property email The email of the customer.
 * @property phoneNo The phone number of the customer.
 * @property postCode The postal code of the customer.
 * @property rentedMedia A mutable arraylist of IDs of media rented by the customer.
 */
data class Customer(
    var customerId: Int,
    var fName: String,
    var lName: String,
    var email: String,
    var phoneNo: String,
    var postCode: String,
    var rentedMedia: MutableList<Int> = mutableListOf()
) {
    /**
     * Function to create a string representation of the Customer object.
     *
     * @return A string representation of the Customer object.
     */
    override fun toString(): String {
        return "Customer Number: $customerId, Name: '$fName $lName', Email: '$email', Phone No.: '$phoneNo', Postcode: '$postCode', IDs of rented media: $rentedMedia"
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
