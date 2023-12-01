package controllers

import models.Customer
import utils.Utilities.formatListString
import java.util.ArrayList

class CustomerAPI() {

    private var customers = ArrayList<Customer>()

    private var lastId = 0
    private fun getId() = lastId++

    fun add(customer: Customer): Boolean {
        customer.customerId = getId()
        return customers.add(customer)
    }

    fun delete(id: Int) = customers.removeIf { customer -> customer.customerId == id }

    fun update(id: Int, customer: Customer?): Boolean {
        val foundCustomer = findCustomer(id)

        if ((foundCustomer != null) && (customer != null)) {
            foundCustomer.fName = customer.fName
            foundCustomer.lName = customer.lName
            foundCustomer.email = customer.email
            foundCustomer.phoneNo = customer.phoneNo
            foundCustomer.postCode = customer.postCode
            return true
        }

        return false
    }

    private fun formatCustomerList(list: List<Customer>): String {
        return list.joinToString("\n")
    }

    fun listAllCustomers() =
        if (customers.isEmpty()) "No customers stored"
        else formatCustomerList(customers)


    fun numberOfCustomers() = customers.size

    fun findCustomer(customerId: Int): Customer? {
        return customers.find { customer -> customer.customerId == customerId }

    }
}
