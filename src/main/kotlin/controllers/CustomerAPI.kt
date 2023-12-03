package controllers

import models.Customer
import persistence.Serializer
import java.util.ArrayList

class CustomerAPI(private val serializerType: Serializer) {

    // Private properties
    private var serializer: Serializer = serializerType
    private var customers = ArrayList<Customer>()

    private var lastId = 0
    private fun getId() = lastId++

    private fun formatListString(customerToFormat: ArrayList<Customer>): String =
        customerToFormat
            .joinToString(separator = "\n") { customer ->
                customers.indexOf(customer).toString() + ": " + customer.toString()
            }


    fun add(customer: Customer): Boolean {
        customer.customerId = getId()
        return customers.add(customer)
    }

    fun listAllCustomers() =
        if (customers.isEmpty()) "No customers stored"
        else formatListString(customers)

    fun numberOfCustomers() = customers.size

    fun findCustomer(customerId: Int): Customer? {
        return customers.find { customer -> customer.customerId == customerId }

    }
    @Throws(Exception::class)
    fun load() {
        customers = serializer.read() as ArrayList<Customer>
    }

    @Throws(Exception::class)
    fun save() {
        serializer.write(customers)
    }
}
