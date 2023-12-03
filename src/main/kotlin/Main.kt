import controllers.CustomerAPI
import controllers.MediaAPI
import models.Customer
import models.Media
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.util.Locale
import kotlin.system.exitProcess

private val mediaAPI = MediaAPI(JSONSerializer(File("media.json")))
private val customerAPI = CustomerAPI(JSONSerializer(File("customer.json")))

private val logger = KotlinLogging.logger {}
var isDataLoaded = false

/**
 * The main function that runs the menu.
 */
fun main() = runMenu()

/**
 * Function to run the main menu.
 */
fun runMenu() {
    if (!isDataLoaded) {
        loadAll()
        isDataLoaded = true
    }
    do {
        when (val option = mainMenu()) {
            1 -> listAllMedias()
            2 -> rentMediaMenu()
            3 -> searchMedias()
            4 -> createNewCustomer()
            5 -> returnRentedMedia()
            6 -> viewCustomer()
            0 -> exitApp()
            997 -> loadAll()
            998 -> saveAll()
            999 -> secretStaffMenu()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

/**
 * Function to display the main menu and read the user's choice.
 * @return The user's menu choice as an Int.
 */
fun mainMenu() = readNextInt(
    """ 
         > ╔═══════════════════════════════════════════════════════════════╗
         > ║  ██████╗██╗███╗   ██╗███████╗██████╗ ██╗     ███████╗██╗  ██╗ ║
         > ║ ██╔════╝██║████╗  ██║██╔════╝██╔══██╗██║     ██╔════╝╚██╗██╔╝ ║
         > ║ ██║     ██║██╔██╗ ██║█████╗  ██████╔╝██║     █████╗   ╚███╔╝  ║
         > ║ ██║     ██║██║╚██╗██║██╔══╝  ██╔═══╝ ██║     ██╔══╝   ██╔██╗  ║
         > ║ ╚██████╗██║██║ ╚████║███████╗██║     ███████╗███████╗██╔╝ ██╗ ║
         > ║  ╚═════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ║
         > ║  Welcome to Cineplex - Please select a number & press 'Enter' ║
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

/**
 * Function to display the secret staff menu and read the user's choice.
 */
fun secretStaffMenu() {
    val password = "secret"
    val inputPassword = readNextLine("Enter the password: ")
    if (inputPassword != password) {
        println("Incorrect password. Access denied.")
        return
    }

    val secretStaffMenu = readNextInt(
        """
        > ╔═══════════════════════════════╗
        > ║                               ║
        > ║    [INTERNAL] STAFF MENU      ║
        > ║                               ║
        > ╠═══════════════════════════════╣
        > ║   1) Add media                ║
        > ║   2) Update media             ║
        > ║   3) Delete media             ║
        > ║   4) Save all databases       ║
        > ║   5) Load all databases       ║
        > ║   10) Back to main menu       ║
        > ╚═══════════════════════════════╝
        > ==>> """.trimMargin(">")
    )

    when (secretStaffMenu) {
        1 -> addMedia()
        2 -> updateMedia()
        3 -> deleteMedia()
        4 -> saveAll()
        5 -> loadAll()
        10 -> runMenu()
        else -> println("Invalid option entered: $secretStaffMenu")
    }
}

// Persistence functions

/**
 * Function to load all data.
 */
fun loadAll() {
    loadCustomer()
    loadMedia()
}

/**
 * Function to save all data.
 */
fun saveAll() {
    saveCustomer()
    saveMedia()
}

// Media functions

/**
 * Function to add a new media.
 */
fun addMedia() {
    val mediaId = mediaAPI.numberOfMedias() + 1
    val mediaTitle = readNextLine("Enter a title for the media: ")
    val mediaRuntime = readNextLine("Enter the runtime for the media: ")
    val mediaGenre = readNextLine("Enter the genre for the media: ")
    val isRented =
        readNextLine("Is the media currently being rented? (yes/no): ").lowercase(Locale.getDefault()) == "no"
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

/**
 * Function to update an existing media.
 */
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

            if (mediaAPI.update(id, media)) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no media found.")
        }
        secretStaffMenu()
    }
}

/**
 * Function to delete a media.
 */
fun deleteMedia() {
    if (mediaAPI.numberOfMedias() > 0) {
        val id = readNextInt("Enter the id of the media to delete: ")
        val mediaToDelete = mediaAPI.delete(id)
        if (mediaToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
        secretStaffMenu()
    }
}

/**
 * Function to list all medias.
 */
fun listAllMedias() = println(mediaAPI.listAllMedias())

/**
 * Function to search medias by title.
 */
fun searchMedias() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = mediaAPI.searchMediasByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No medias found")
    } else {
        println(searchResults)
    }
}

/**
 * Function to rent a media.
 */
fun rentMediaMenu() {
    val customerId = readNextInt("Enter the customer id: ")
    val customer = customerAPI.findCustomer(customerId)
    if (customer != null) {
        println("All media: ")
        for (mediaId in customer.rentedMedia) {
            val media = mediaAPI.findMedia(mediaId)
            if (media != null) {
                println("Media ID: $mediaId, Title: ${media.mediaTitle}, Available: ${!media.isRented}")
            }
        }
        do {
            val mediaId = readNextInt("Enter the media id to rent (or -1 to stop): ")
            if (mediaId == -1) {
                break
            }
            val media = mediaAPI.findMedia(mediaId)
            if (media != null && !media.isRented) {
                customer.rentedMedia.add(mediaId)
                media.isRented = true
                println("Media rented successfully. Enjoy!")
            } else {
                println("Failed to rent media. It might not be available to rent.")
            }
        } while (true)
    } else {
        println("Customer not found.")
    }
}

/**
 * Function to return a rented media.
 */
fun returnRentedMedia() {
    val customerId = readNextInt("Enter the customer id: ")
    val customer = customerAPI.findCustomer(customerId)
    if (customer != null) {
        println("Currently rented media by the customer: ")
        for (mediaId in customer.rentedMedia) {
            val media = mediaAPI.findMedia(mediaId)
            if (media != null) {
                println("Media ID: $mediaId, Title: ${media.mediaTitle}")
            }
        }
        do {
            val mediaId = readNextInt("Enter the media id to return (or -1 to stop): ")
            if (mediaId == -1) {
                break
            }
            val media = mediaAPI.findMedia(mediaId)
            if (media != null && media.isRented) {
                customer.rentedMedia.remove(mediaId)
                media.isRented = false
                println("Media returned successfully. Hope you enjoyed!")
            } else {
                println("Failed to return media. Please try again later.")
            }
        } while (true)
    } else {
        println("Customer not found.")
    }
}

// Customer functions

/**
 * Function to view a customer's details.
 */
fun viewCustomer() {
    val customerId = readNextInt("Enter the customer id: ")
    val customer = customerAPI.findCustomer(customerId)
    if (customer != null) {
        println(customer)
    } else {
        println("Customer not found.")
    }
}

/**
 * Function to create a new customer.
 */
fun createNewCustomer() {
    val customerId = customerAPI.numberOfCustomers() + 1
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

// Persistence functions

/**
 * Function to save customer data.
 */
fun saveCustomer() {
    try {
        customerAPI.save()
        println("Save successful")
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Function to load customer data.
 */
fun loadCustomer() {
    try {
        customerAPI.load()
        println("Load successful")
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/**
 * Function to save media data.
 */
fun saveMedia() {
    try {
        mediaAPI.save()
        println("Save successful")
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Function to load media data.
 */
fun loadMedia() {
    try {
        mediaAPI.load()
        println("Load successful")
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/**
 * Function to exit the application.
 */
fun exitApp() {
    saveCustomer()
    saveMedia()
    println("Exiting...bye")
    exitProcess(0)
}
