import controllers.CustomerAPI
import controllers.MediaAPI
import models.Customer
import models.Media
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.util.*
import kotlin.system.exitProcess

private val mediaAPI = MediaAPI(JSONSerializer(File("media.json")))
private val customerAPI = CustomerAPI(JSONSerializer(File("customer.json")))

// Initialize a logger for logging messages
private val logger = KotlinLogging.logger {}

var lastMediaId = 0 //MediaID incrementation
var lastCustomerId = 0 //CustomerID incrementation
fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> listAllMedias()
            2 -> rentMediaMenu()
            3 -> searchMedias()
            4 -> createNewCustomer()
            5 -> returnRentedMedia()
            6 -> viewCustomer()
            0 -> exitApp()
            998 -> secretStaffMenu()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
        """ 
         > ╔═══════════════════════════════════════════════════════════════╗
         > ║  ██████╗██╗███╗   ██╗███████╗██████╗ ██╗     ███████╗██╗  ██╗ ║
         > ║ ██╔════╝██║████╗  ██║██╔════╝██╔══██╗██║     ██╔════╝╚██╗██╔╝ ║
         > ║ ██║     ██║██╔██╗ ██║█████╗  ██████╔╝██║     █████╗   ╚███╔╝  ║
         > ║ ██║     ██║██║╚██╗██║██╔══╝  ██╔═══╝ ██║     ██╔══╝   ██╔██╗  ║
         > ║ ╚██████╗██║██║ ╚████║███████╗██║     ███████╗███████╗██╔╝ ██╗ ║
         > ║  ╚═════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ║
         > ╠═══════════════════════════════════════════════════════════════╣
         > ║  [1] View all available media to rent                         ║
         > ║  [2] Rent out media                                           ║
         > ║  [3] Search media by term                                     ║
         > ╠═══════════════════════════════════════════════════════════════╣
         > ║  [4] Create new account                                       ║
         > ║  [5] Return rented media                                      ║
         > ║  [6] View account                                             ║
         > ╠═══════════════════════════════════════════════════════════════╣
         > ║  [0] Exit                                                     ║
         > ╚═══════════════════════════════════════════════════════════════╝                 
         > ==>> """.trimMargin(">")
    )

fun secretStaffMenu() {
    val option = readNextInt(
        """
        > ╔═══════════════════════════════╗
        > ║                               ║
        > ║    [INTERNAL] STAFF MENU      ║
        > ║                               ║
        > ╠═══════════════════════════════╣
        > ║   1) Add media                ║
        > ║   2) Update media             ║
        > ║   3) Delete media             ║
        > ║   4) Save customer            ║
        > ║   5) Save media               ║
        > ║   6) Load customer            ║
        > ║   7) Load media               ║
        > ║   8) Back to main menu        ║
        > ╚═══════════════════════════════╝
        > ==>> """.trimMargin(">"))

    when (option) {
        1 -> addMedia()
        2 -> updateMedia()
        3 -> deleteMedia()
        4 -> saveCustomer()
        5 -> saveMedia()
        6 -> loadCustomer()
        7 -> loadMedia()
        8 -> runMenu()
        else -> println("Invalid option entered: $option")
    }
}

fun addMedia() {
    val mediaId = lastMediaId++
    val mediaTitle = readNextLine("Enter a title for the media: ")
    val mediaRuntime = readNextLine("Enter the runtime for the media: ")
    val mediaGenre = readNextLine("Enter the genre for the media: ")
    val isRented =
        readNextLine("Is the media currently being rented? (yes/no): ").lowercase(Locale.getDefault()) == "yes"
    val isAdded = mediaAPI.add(
        Media(
            mediaId = mediaId,
            mediaTitle = mediaTitle,
            mediaRuntime = mediaRuntime,
            mediaGenre = mediaGenre,
            isRented = isRented
        )
    )

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
    secretStaffMenu()
}

fun updateMedia() {
    if (mediaAPI.numberOfMedias() > 0) {
        val id = readNextInt("Enter the id of the media to update: ")
        val media = mediaAPI.findMedia(id)
        if (media != null) {
            val mediaTitle = readNextLine("Enter a title for the media: ")
            val mediaRuntime = readNextLine("Enter the runtime for the media: ")
            val mediaGenre = readNextLine("Enter the genre for the media: ")

            media.mediaTitle = mediaTitle
            media.mediaRuntime = mediaRuntime
            media.mediaGenre = mediaGenre

            if (mediaAPI.update(id, media)){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no medias for this index number")
        }
    }
}


fun deleteMedia() {
    if (mediaAPI.numberOfMedias() > 0) {
        // only ask the user to choose the media to delete if medias exist
        val id = readNextInt("Enter the id of the media to delete: ")
        // pass the index of the media to MediaAPI for deleting and check for success.
        val mediaToDelete = mediaAPI.delete(id)
        if (mediaToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun listAllMedias() = println(mediaAPI.listAllMedias())

fun searchMedias() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = mediaAPI.searchMediasByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No medias found")
    } else {
        println(searchResults)
    }
}

fun rentMediaMenu() {
    val customerId = readNextInt("Enter the customer id: ")
    val mediaId = readNextInt("Enter the media id to rent: ")
    val customer = customerAPI.findCustomer(customerId)
    val media = mediaAPI.findMedia(mediaId)
    if (customer != null && media != null && !media.isRented) {
        customer.rentedMedia.add(mediaId)
        media.isRented = true
        println("Media rented successfully.")
    } else {
        println("Failed to rent media.")
    }
}

fun returnRentedMedia() {
    val customerId = readNextInt("Enter the customer id: ")
    val mediaId = readNextInt("Enter the media id to return: ")
    val customer = customerAPI.findCustomer(customerId)
    val media = mediaAPI.findMedia(mediaId)
    if (customer != null && media != null && media.isRented) {
        customer.rentedMedia.remove(mediaId)
        media.isRented = false
        println("Media returned successfully.")
    } else {
        println("Failed to return media.")
    }
}


// Customer functions

fun viewCustomer() {
    val customerId = readNextInt("Enter the customer id: ")
    val customer = customerAPI.findCustomer(customerId) // Use the findCustomer function from CustomerAPI
    if (customer != null) {
        println(customer)
    } else {
        println("Customer not found.")
    }
}

fun createNewCustomer() {
    val customerId = lastCustomerId++
    val fName = readNextLine("Enter the first name of the customer: ")
    val lName = readNextLine("Enter the last name of the customer: ")
    val email = readNextLine("Enter the email of the customer: ")
    val phoneNo = readNextLine("Enter the phone number of the customer: ")
    val postCode = readNextLine("Enter the postal code of the customer: ")
    val customer = Customer(
        customerId = customerId,
        fName = fName,
        lName = lName,
        email = email,
        phoneNo = phoneNo,
        postCode = postCode
    )
    val isAdded = customerAPI.add(customer)

    if (isAdded) {
        println("Customer added successfully")
    } else {
        println("Failed to add customer")
    }
}



fun saveCustomer() {
    try {
        customerAPI.save()
        println("Save successful")
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun loadCustomer() {
    try {
        customerAPI.load()
        println("Load successful")
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun saveMedia() {
    try {
        mediaAPI.save()
        println("Save successful")
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun loadMedia() {
    try {
        mediaAPI.load()
        println("Load successful")
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    saveCustomer()
    saveMedia()
    println("Exiting...bye")
    exitProcess(0)
}
