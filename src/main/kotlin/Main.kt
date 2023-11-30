import controllers.MediaAPI
import models.Customer
import models.Media
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess

private val mediaAPI = MediaAPI()

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addMedia()
            2 -> listMedias()
            3 -> updateMedia()
            4 -> deleteMedia()
            10 -> searchMedias()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
        """ 
         > -----------------------------------------------------  
         > |                  MEDIA KEEPER APP                  |
         > -----------------------------------------------------  
         > | MEDIA MENU                                         |
         > |   1) Add a media                                   |
         > |   2) List medias                                   |
         > |   3) Update a media                                |
         > |   4) Delete a media                                |
         > |   5) Archive a media                               |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > |   6) Add customer to a media                           |
         > |   7) Update customer contents on a media               |
         > |   8) Delete customer from a media                      |
         > |   9) Mark customer as complete/todo                   | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR MEDIAS                             | 
         > |   10) Search for all medias (by media title)        |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR ITEMS                             |                                
         > |   15) Search for all customers (by customer description)  |
         > |   16) List TODO Customers                             |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
    )

//------------------------------------
//MEDIA MENU
//------------------------------------
fun addMedia() {
    val mediaTitle = readNextLine("Enter a title for the media: ")
    val mediaPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val mediaCategory = readNextLine("Enter a category for the media: ")
    val isAdded = mediaAPI.add(Media(mediaTitle = mediaTitle, mediaPriority = mediaPriority, mediaCategory = mediaCategory))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listMedias() {
    if (mediaAPI.numberOfMedias() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL medias          |
                  > |   2) View ACTIVE medias       |
                  > |   3) View ARCHIVED medias     |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllMedias()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No medias stored")
    }
}

fun listAllMedias() = println(mediaAPI.listAllMedias())

fun updateMedia() {
    listMedias()
    if (mediaAPI.numberOfMedias() > 0) {
        // only ask the user to choose the media if medias exist
        val id = readNextInt("Enter the id of the media to update: ")
        if (mediaAPI.findMedia(id) != null) {
            val mediaTitle = readNextLine("Enter a title for the media: ")
            val mediaPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val mediaCategory = readNextLine("Enter a category for the media: ")

            // pass the index of the media and the new media details to MediaAPI for updating and check for success.
            if (mediaAPI.update(id, Media(0, mediaTitle, mediaPriority, mediaCategory, false))){
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
    listMedias()
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

fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}
