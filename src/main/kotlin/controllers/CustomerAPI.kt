package controllers

import models.Customer
import persistence.Serializer
import java.util.ArrayList

/**
 * This class provides an API for managing customers.
 *
 * @property serializerType The type of serializer to use for persistence.
 */
class CustomerAPI(private val serializerType: Serializer) {

    // Private properties
    private var serializer: Serializer = serializerType
    private var customers = ArrayList<Customer>()

    private var lastId = 0

    /**
     * Function to generate a unique ID for each customer.
     *
     * @return A unique ID.
     */
    private fun getId() = lastId++

    /**
     * Function to format a list of customers into a string.
     *
     * @param customerToFormat The list of customers to format.
     * @return A string representation of the list of customers.
     */
    private fun formatListString(customerToFormat: ArrayList<Customer>): String =
        customerToFormat
            .joinToString(separator = "\n") { customer ->
                customers.indexOf(customer).toString() + ": " + customer.toString()
            }

    /**
     * Function to add a new customer.
     *
     * @param customer The customer to add.
     * @return A boolean indicating whether the operation was successful.
     */
    fun add(customer: Customer): Boolean {
        customer.customerId = getId()
        return customers.add(customer)
    }

    /**
     * Function to list all customers.
     *
     * @return A string representation of all customers.
     */
    fun listAllCustomers() =
        if (customers.isEmpty()) {
            "No customers stored"
        } else {
            formatListString(customers)
        }

    /**
     * Function to get the number of customers.
     *
     * @return The number of customers.
     */
    fun numberOfCustomers() = customers.size

    /**
     * Function to find a customer by their ID.
     *
     * @param customerId The ID of the customer to find.
     * @return The customer if found, null otherwise.
     */
    fun findCustomer(customerId: Int): Customer? {
        return customers.find { customer -> customer.customerId == customerId }
    }

    /**
     * Function to load customers from persistent storage.
     *
     * @throws Exception If an error occurs during loading.
     */
    @Throws(Exception::class)
    fun load() {
        customers = serializer.read() as ArrayList<Customer>
    }

    /**
     * Function to save customers to persistent storage.
     *
     * @throws Exception If an error occurs during saving.
     */
    @Throws(Exception::class)
    fun save() {
        serializer.write(customers)
    }
}
