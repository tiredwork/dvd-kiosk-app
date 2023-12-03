package controllers

import models.Media
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MediaAPITest {

    private lateinit var mediaAPI: MediaAPI
    private lateinit var customerAPI: CustomerAPI

    private var joker: Media? = null
    private var spiderman: Media? = null
    private var johnWick: Media? = null
    private var metallica: Media? = null
    private var taylorSwift: Media? = null

    @BeforeEach
    fun setup() {
        mediaAPI = MediaAPI(JSONSerializer(File("mediaTest.json")))
        customerAPI = CustomerAPI(JSONSerializer(File("customerTest.json")))

        joker = Media(0, "Joker", "122 mins", "Feature, Drama", false)
        spiderman = Media(1, "Spider-Man: Homecoming", "133 mins", "Feature, Adventure", false)
        johnWick = Media(2, "John Wick: Chapter 3 - Parabellum ", "131 mins", "Feature, Action", false)
        metallica = Media(3, "Master of Puppets - Metallica", "54 mins", "Thrash Metal", false)
        taylorSwift = Media(4, "1989 - Taylor Swift", "48 mins", "Synth-pop", false)

        mediaAPI.add(joker!!)
        mediaAPI.add(spiderman!!)
        mediaAPI.add(johnWick!!)
        mediaAPI.add(metallica!!)
        mediaAPI.add(taylorSwift!!)
    }

    @AfterEach
    fun tearDown() {
        joker = null
        spiderman = null
        johnWick = null
        metallica = null
        taylorSwift = null
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

    @Nested
    inner class MediaGetterAndSetterTests {

        @Test
        fun testMediaGetters() {
            val media = Media(0, "Joker", "122 mins", "Feature, Drama", false)
            assertEquals(0, media.mediaId)
            assertEquals("Joker", media.mediaTitle)
            assertEquals("122 mins", media.mediaRuntime)
            assertEquals("Feature, Drama", media.mediaGenre)
            assertEquals(false, media.isRented)
            assertFalse(media.isRented)
        }

        @Test
        fun `Customer setter tests`() {
            val media = Media(0, "Joker", "122 mins", "Feature, Drama", false)
            media.mediaId = 0
            media.mediaTitle = "Joker"
            media.mediaRuntime = "122 mins"
            media.mediaGenre = "Feature, Drama"
            media.isRented = false
            assertEquals(0, media.mediaId)
            assertEquals("Joker", media.mediaTitle)
            assertEquals("122 mins", media.mediaRuntime)
            assertEquals("Feature, Drama", media.mediaGenre)
            assertEquals(false, media.isRented)
            assertFalse(media.isRented)
        }
    }

    @Nested
    inner class StringFormat {
        @Test
        fun `Testing formatListString`() {
            val media = Media(0, "Joker", "122 mins", "Feature, Drama", false)
            val list = listOf("Joker", "Spider-Man", "John Wick")
            val expected = "Joker\nSpider-Man\nJohn Wick"
            assertEquals(expected, media.formatListString(list))
        }
    }
}
