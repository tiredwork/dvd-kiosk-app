import controllers.CustomerAPI
import controllers.MediaAPI
import models.Media
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import persistence.JSONSerializer
import java.io.File
import kotlin.test.Test

class MediaAPITest {

    private lateinit var mediaAPI: MediaAPI
    private lateinit var customerAPI: CustomerAPI

    private var media0: Media? = null
    private var media1: Media? = null
    private var media2: Media? = null
    private var media3: Media? = null
    private var media4: Media? = null

    @BeforeEach
    fun setup() {
        mediaAPI = MediaAPI(JSONSerializer(File("mediaTest.json")))
        customerAPI = CustomerAPI(JSONSerializer(File("customerTest.json")))

        media0 = Media(0, "Joker", "122 mins", "Feature, Drama", false)
        media1 = Media(1, "Spider-Man: Homecoming", "133 mins", "Feature, Adventure", false)
        media2 = Media(2, "John Wick: Chapter 3 - Parabellum ", "131 mins", "Feature, Action", false)
        media3 = Media(3, "Master of Puppets - Metallica", "54 mins", "Thrash Metal", false)
        media4 = Media(4, "1989 - Taylor Swift", "48 mins", "Synth-pop", false)

        mediaAPI.add(media0!!)
        mediaAPI.add(media1!!)
        mediaAPI.add(media2!!)
        mediaAPI.add(media3!!)
        mediaAPI.add(media4!!)
    }

    @AfterEach
    fun tearDown() {
        media0 = null
        media1 = null
        media2 = null
        media3 = null
        media4 = null
    }

    @Nested
    inner class AddMedia {

        @Test
        fun `adding a Media to a populated list adds to ArrayList`() {
            val newMedia = Media(5, "Joker", "122 mins", "Feature, Drama", false)
            assertEquals(5, mediaAPI.numberOfMedias())
            assertTrue(mediaAPI.add(newMedia))
            assertEquals(6, mediaAPI.numberOfMedias())
            assertEquals(newMedia, mediaAPI.findMedia(5))
        }

        @Test
        fun `adding a Media to an empty list adds to ArrayList`() {
            val mediaAPIEmpty = MediaAPI(JSONSerializer(File("mediaTest.json")))
            val newMedia = Media(0, "Joker", "122 mins", "Feature, Drama", false)
            assertEquals(0, mediaAPIEmpty.numberOfMedias())
            assertTrue(mediaAPIEmpty.add(newMedia))
            assertEquals(1, mediaAPIEmpty.numberOfMedias())
            assertEquals(newMedia, mediaAPIEmpty.findMedia(0))
        }
    }

    @Nested
    inner class ListMedia {

        @Nested
        inner class ListAllMedia {
            @Test
            fun `listAllMedia returns No Media Stored message when ArrayList is empty`() {
                val mediaAPIEmpty = MediaAPI(JSONSerializer(File("mediaTest.json")))
                assertEquals(0, mediaAPIEmpty.numberOfMedias())
                assertTrue(mediaAPIEmpty.listAllMedias().lowercase().contains("no medias stored"))
            }


            @Test
            fun `listAllMedia returns Media when ArrayList has Media stored`() {
                assertEquals(5, mediaAPI.numberOfMedias())
                val mediaString = mediaAPI.listAllMedias().lowercase()
                assertTrue(mediaString.contains("joker"))
                assertTrue(mediaString.contains("122 mins"))
                assertTrue(mediaString.contains("feature, drama"))
                assertTrue(mediaString.contains("spider-man: homecoming"))
                assertTrue(mediaString.contains("133 mins"))
                assertTrue(mediaString.contains("feature, adventure"))
                assertTrue(mediaString.contains("john wick: chapter 3 - parabellum"))
                assertTrue(mediaString.contains("131 mins"))
                assertTrue(mediaString.contains("feature, action"))
                assertTrue(mediaString.contains("master of puppets - metallica"))
                assertTrue(mediaString.contains("54 mins"))
                assertTrue(mediaString.contains("thrash metal"))
                assertTrue(mediaString.contains("1989 - taylor swift"))
                assertTrue(mediaString.contains("48 mins"))
                assertTrue(mediaString.contains("synth-pop"))
            }
        }
    }
}
