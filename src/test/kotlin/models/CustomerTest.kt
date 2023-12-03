package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CustomerTest {

    @Test
    fun `test toString method`() {
        val customer = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", rentedMedia = mutableListOf(0, 1))
        val expected = "Customer Number: 0, Name: 'Jane Smith', Email: 'jane.smith@email.com', Phone No.: '123-456-789', Postcode: '12345', IDs of rented media: [0, 1]"
        assertEquals(expected, customer.toString())
    }

    @Test
    fun `test formatListString method`() {
        val customer = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", rentedMedia = mutableListOf(0, 1))
        val list = listOf("Item1", "Item2", "Item3")
        val expected = "Item1\nItem2\nItem3"
        assertEquals(expected, customer.formatListString(list))
    }
}
