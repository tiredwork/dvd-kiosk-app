import controllers.CustomerAPI
import controllers.MediaAPI
import models.Customer
import models.Media
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import java.io.File
import kotlin.test.assertEquals

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

    @BeforeEach
    fun setup() {
        mediaAPI = MediaAPI(JSONSerializer(File("mediaTest.json")))
        customerAPI = CustomerAPI(JSONSerializer(File("customerTest.json")))

        mediaAPI.add(Media(0, "Media 0", "Runtime 0", "Genre 0", false))
        mediaAPI.add(Media(1, "Media 1", "Runtime 1", "Genre 1", false))

        janeSmith = Customer(0, "Jane", "Smith", "jane.smith@email.com", "123-456-789", "12345", rentedMedia = mutableListOf(0,1))
        bobWilliams = Customer(1, "Bob", "Williams", "bob.williams@email.com", "123-456-789", "12346", rentedMedia = mutableListOf(0))
        aliceJohnson = Customer(2, "Alice", "Johnson", "alice.johnson@email.com", "123-456-789", "12347", rentedMedia = mutableListOf(1))
        charlieBrown = Customer(3, "Charlie", "Brown", "charlie.brown@email.com", "123-456-789", "12348", rentedMedia = mutableListOf(1,0))
        davidSmith = Customer(4, "David", "Smith", "david.smith@email.com", "123-456-789", "12349", rentedMedia = mutableListOf())

        populatedCustomers!!.add(janeSmith!!)
        populatedCustomers!!.add(bobWilliams!!)
        populatedCustomers!!.add(aliceJohnson!!)
        populatedCustomers!!.add(charlieBrown!!)
        populatedCustomers!!.add(davidSmith!!)
    }

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

        @Test
        fun `adding a Customer to a populated list adds to ArrayList`() {
            val newCustomer = Customer(5, "Jane", "Doe", "jane.doe@email.com", "123-456-7890", "12345", mutableListOf())
            assertEquals(5, populatedCustomers!!.numberOfCustomers())
            assertTrue(populatedCustomers!!.add(newCustomer))
            assertEquals(6, populatedCustomers!!.numberOfCustomers())
            assertEquals(newCustomer, populatedCustomers!!.findCustomer(5))
        }

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
            @Test
            fun `listAllCustomers returns No Customers Stored message when ArrayList is empty`() {
                assertEquals(0, emptyCustomers!!.numberOfCustomers())
                assertTrue(emptyCustomers!!.listAllCustomers().lowercase().contains("no customers stored"))
            }

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

        @Test
        fun `saving and loading an loaded collection in JSON doesn't lose data`() {
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

        @Test
        fun numberOfCustomersCalculatedCorrectly() {
            assertEquals(5, populatedCustomers!!.numberOfCustomers())
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
        }
    }
}
}
