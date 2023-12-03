package controllers

import models.Customer
import models.Media
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CustomerAPITest {

    private lateinit var customerAPI: CustomerAPI
    private lateinit var mediaAPI: MediaAPI

    private var janeSmith: Customer? = null
    private var bobWilliams: Customer? = null
    private var aliceJohnson: Customer? = null
    private var charlieBrown: Customer? = null
    private var davidSmith: Customer? = null
    private var populatedCustomers: CustomerAPI? = CustomerAPI(JSONSerializer(File("customerTest.json")))
    private var emptyCustomers: CustomerAPI? = CustomerAPI(JSONSerializer(File("customerTest.json")))

    /**
     * This method is executed before each test. It sets up the test environment.
     */
    @BeforeEach
    fun setup() {
        mediaAPI = MediaAPI(JSONSerializer(File("mediaTest.json")))
        customerAPI = CustomerAPI(JSONSerializer(File("customerTest.json")))

        mediaAPI.add(Media(0, "Media 0", "Runtime 0", "Genre 0", false))
        mediaAPI.add(Media(1, "Media 1", "Runtime 1", "Genre 1", false))

        janeSmith = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", rentedMedia = mutableListOf(0, 1))
        bobWilliams = Customer(1, "Bob", "Williams", "bob.williams@email.com", "123-456-789", "12346", rentedMedia = mutableListOf(0))
        aliceJohnson = Customer(2, "Alice", "Johnson", "alice.johnson@email.com", "123-456-789", "12347", rentedMedia = mutableListOf(1))
        charlieBrown = Customer(3, "Charlie", "Brown", "charlie.brown@email.com", "123-456-789", "12348", rentedMedia = mutableListOf(1, 0))
        davidSmith = Customer(4, "David", "Smith", "david.smith@email.com", "123-456-789", "12349", rentedMedia = mutableListOf())

        populatedCustomers!!.add(janeSmith!!)
        populatedCustomers!!.add(bobWilliams!!)
        populatedCustomers!!.add(aliceJohnson!!)
        populatedCustomers!!.add(charlieBrown!!)
        populatedCustomers!!.add(davidSmith!!)
    }

    /**
     * This method is executed after each test. It cleans up the test environment.
     */
    @AfterEach
    fun tearDown() {
        janeSmith = null
        bobWilliams = null
        aliceJohnson = null
        charlieBrown = null
        davidSmith = null
        populatedCustomers = null
        emptyCustomers = null
    }

    @Nested
    inner class AddCustomers {

        /**
         * Test case for adding a Customer to a populated list.
         */
        @Test
        fun `adding a Customer to a populated list adds to ArrayList`() {
            val newCustomer = Customer(5, "Jane", "Doe", "jane.doe@email.com", "123-456-7890", "12345", mutableListOf())
            assertEquals(5, populatedCustomers!!.numberOfCustomers())
            assertTrue(populatedCustomers!!.add(newCustomer))
            assertEquals(6, populatedCustomers!!.numberOfCustomers())
            assertEquals(newCustomer, populatedCustomers!!.findCustomer(5))
        }

        /**
         * Test case for adding a Customer to an empty list.
         */
        @Test
        fun `adding a Customer to an empty list adds to ArrayList`() {
            val newCustomer = Customer(0, "Jane", "Doe", "jane.doe@email.com", "123-456-7890", "12345", mutableListOf())
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertTrue(emptyCustomers!!.add(newCustomer))
            assertEquals(1, emptyCustomers!!.numberOfCustomers())
            assertEquals(newCustomer, emptyCustomers!!.findCustomer(0))
        }
    }

    @Nested
    inner class ListCustomers {

        @Nested
        inner class ListAllCustomers {

            /**
             * Test case for listing all customers when ArrayList is empty.
             */
            @Test
            fun `listAllCustomers returns No Customers Stored message when ArrayList is empty`() {
                assertEquals(0, emptyCustomers!!.numberOfCustomers())
                assertTrue(emptyCustomers!!.listAllCustomers().lowercase().contains("no customers stored"))
            }

            /**
             * Test case for listing all customers when ArrayList has Customers stored.
             */
            @Test
            fun `listAllCustomers returns Customers when ArrayList has Customers stored`() {
                assertEquals(5, populatedCustomers!!.numberOfCustomers())
                val customersString = populatedCustomers!!.listAllCustomers().lowercase()
                assertTrue(customersString.contains("jane"))
                assertTrue(customersString.contains("smith"))
                assertTrue(customersString.contains("bob"))
                assertTrue(customersString.contains("williams"))
                assertTrue(customersString.contains("alice"))
                assertTrue(customersString.contains("johnson"))
                assertTrue(customersString.contains("charlie"))
                assertTrue(customersString.contains("brown"))
                assertTrue(customersString.contains("david"))
                assertTrue(customersString.contains("smith"))
            }
        }

        @Nested
        inner class PersistenceTests {

            /**
             * Test case for saving and loading an empty collection in JSON.
             */
            @Test
            fun `saving and loading an empty collection in JSON doesn't crash app`() {
                val storingCustomers = CustomerAPI(JSONSerializer(File("customerTest.json")))
                storingCustomers.save()

                val loadedCustomers = CustomerAPI(JSONSerializer(File("customerTest.json")))
                loadedCustomers.load()

                assertEquals(0, storingCustomers.numberOfCustomers())
                assertEquals(0, loadedCustomers.numberOfCustomers())
                assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
            }

            /**
             * Test case for saving and loading a loaded collection in JSON.
             */
            @Test
            fun `saving and loading a loaded collection in JSON doesn't lose data`() {
                val storingCustomers = CustomerAPI(JSONSerializer(File("customerTest.json")))
                storingCustomers.add(janeSmith!!)
                storingCustomers.add(bobWilliams!!)
                storingCustomers.add(aliceJohnson!!)
                storingCustomers.save()

                val loadedCustomers = CustomerAPI(JSONSerializer(File("customerTest.json")))
                loadedCustomers.load()

                assertEquals(3, storingCustomers.numberOfCustomers())
                assertEquals(3, loadedCustomers.numberOfCustomers())
                assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
                assertEquals(storingCustomers.findCustomer(0), loadedCustomers.findCustomer(0))
                assertEquals(storingCustomers.findCustomer(1), loadedCustomers.findCustomer(1))
                assertEquals(storingCustomers.findCustomer(2), loadedCustomers.findCustomer(2))
            }
        }

        @Nested
        inner class CountingMethods {

            /**
             * Test case for counting number of customers when populated and empty.
             */
            @Test
            fun `Count number of customers when populated and empty`() {
                assertEquals(5, populatedCustomers!!.numberOfCustomers())
                assertEquals(0, emptyCustomers!!.numberOfCustomers())
            }
        }

        @Nested
        inner class GetterAndSetterTests {

            /**
             * Test case for Customer getter methods.
             */
            @Test
            fun `Customer getter tests`() {
                val customer = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", mutableListOf())
                assertEquals(0, customer.customerId)
                assertEquals("Jane", customer.fName)
                assertEquals("Smith", customer.lName)
                assertEquals("jane.smith@email.com", customer.email)
                assertEquals("123-456-789", customer.phoneNo)
                assertEquals("12345", customer.postCode)
                assertTrue(customer.rentedMedia.isEmpty())
            }

            /**
             * Test case for Customer setter methods.
             */
            @Test
            fun `Customer setter tests`() {
                val customer = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", mutableListOf(0))
                customer.fName = "Jane"
                customer.lName = "Smith"
                customer.email = "jane.smith@email.com"
                customer.phoneNo = "123-456-789"
                customer.postCode = "12345"
                customer.rentedMedia.add(1)
                assertEquals("Jane", customer.fName)
                assertEquals("Smith", customer.lName)
                assertEquals("jane.smith@email.com", customer.email)
                assertEquals("123-456-789", customer.phoneNo)
                assertEquals("12345", customer.postCode)
                assertEquals(2, customer.rentedMedia.size)
                assertTrue(customer.rentedMedia.contains(1))
            }
        }

        @Nested
        inner class StringFormat {

            /**
             * Test case for formatting a list into a string.
             */
            @Test
            fun `Testing formatListString`() {
                val customer = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", rentedMedia = mutableListOf(0, 1))
                val list = listOf("Jane", "Bob", "Alice")
                val expected = "Jane\nBob\nAlice"
                assertEquals(expected, customer.formatListString(list))
            }
        }
    }
}
