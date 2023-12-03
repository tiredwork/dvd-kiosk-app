package models

data class Customer(
    var customerId: Int,
    var fName: String,
    var lName: String,
    var email: String,
    var phoneNo: String,
    var postCode: String,
    var rentedMedia: MutableList<Int> = mutableListOf()
){
    override fun toString(): String {
        return "Customer Number: $customerId, Name: '$fName $lName', Email: '$email', Phone No.: '$phoneNo', Postcode: '$postCode', IDs of rented media: $rentedMedia"
    }
    fun formatListString(list: List<Any>): String {
        return list.joinToString("\n")
    }
}