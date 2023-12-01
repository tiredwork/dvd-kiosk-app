import controllers.CustomerAPI
import controllers.MediaAPI
import models.Customer
import models.Media
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.util.*
import kotlin.system.exitProcess

private val mediaAPI = MediaAPI()
private val CustomerAPI = CustomerAPI()

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> listAllMedias()
            2 -> searchMedias()
            3 -> returnRentedMedia()
            4 -> viewCustomer()
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
         > ║  [2] Search media by term                                     ║
         > ╠═══════════════════════════════════════════════════════════════╣
         > ║  [3] Return rented media                                      ║
         > ║  [4] View account                                             ║
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
        > ╚═══════════════════════════════╝
        > ==>> """.trimMargin(">"))

    when (option) {
        1 -> addMedia()
        2 -> updateMedia()
        3 -> deleteMedia()
        else -> println("Invalid option entered: $option")
    }
}

fun addMedia() {
    val mediaTitle = readNextLine("Enter a title for the media: ")
    val mediaRuntime = readNextInt("Enter the runtime for the media: ")
    val mediaGenre = readNextLine("Enter the genre for the media: ")
    val isRented =
        readNextLine("Is the media currently being rented? (yes/no): ").lowercase(Locale.getDefault()) == "yes"
    val isAdded = mediaAPI.add(
        Media(
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
}

fun listAllMedias() = println(mediaAPI.listAllMedias())

fun updateMedia() {
    if (mediaAPI.numberOfMedias() > 0) {
        // only ask the user to choose the media if medias exist
        val id = readNextInt("Enter the id of the media to update: ")
        if (mediaAPI.findMedia(id) != null) {
            val mediaTitle = readNextLine("Enter a title for the media: ")
            val mediaRuntime = readNextInt("Enter the runtime for the media: ")
            val mediaGenre = readNextLine("Enter the genre for the media: ")

            // pass the index of the media and the new media details to MediaAPI for updating and check for success.
            if (mediaAPI.update(id, Media(0, mediaTitle, mediaRuntime, mediaGenre, isRented = false))){
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
fun searchMedias() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = mediaAPI.searchMediasByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No medias found")
    } else {
        println(searchResults)
    }
}

fun returnRentedMedia() {
    val customerId = readNextInt("Enter the customer id: ")
    val mediaId = readNextInt("Enter the media id to return: ")
    val customer = CustomerAPI.findCustomer(customerId) // Use the findCustomer function from CustomerAPI
    if (customer != null) {
        val media = mediaAPI.findMedia(mediaId)
        if (media != null) {
            customer.returnRentedMedia(media) // Call returnRentedMedia on the customer instance
        } else {
            println("Media not found.")
        }
    } else {
        println("Customer not found.")
    }
}

fun viewCustomer() {
    val customerId = readNextInt("Enter the customer id: ")
    val customer = CustomerAPI.findCustomer(customerId) // Use the findCustomer function from CustomerAPI
    if (customer != null) {
        println(customer)
    } else {
        println("Customer not found.")
    }
}


fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}
