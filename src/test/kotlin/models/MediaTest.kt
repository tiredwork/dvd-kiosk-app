package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MediaTest {

    @Test
    fun `test toString method`() {
        val media = Media(0, "Joker", "122 mins", "Feature, Drama", false)
        val expected = "Media ID: 0: 'Joker', Runtime: '122 mins', Genre: 'Feature, Drama', Is it Rented?: false"
        assertEquals(expected, media.toString())
    }

    @Test
    fun `test formatListString method`() {
        val media = Media(0, "Joker", "122 mins", "Feature, Drama", false)
        val list = listOf("Joker", "Spider-Man", "John Wick")
        val expected = "Joker\nSpider-Man\nJohn Wick"
        assertEquals(expected, media.formatListString(list))
    }
}
