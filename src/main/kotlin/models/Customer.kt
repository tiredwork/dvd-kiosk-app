package models

data class Customer (var customerId: Int = 0, var customerContents : String, var isCustomerComplete: Boolean = false){

    override fun toString(): String {

        return "$customerId: $customerContents, Priority($isCustomerComplete)"
    }
}