package com.tanelso2.slaythespirecardbot

import com.tanelso2.slaythespirecardbot.CardPattern.getCards
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardPatternTest {

    @Test
    fun getCardsTest_noCards() {
        val body = "Hello World"
        assertTrue(getCards(body).isEmpty())
    }

    @Test
    fun getCardsTest_oneCard() {
        val card = "Adaptive AI"
        val body = "I've literally never picked up [[$card]]"
        val result = getCards(body)
        assertEquals(1, result.size)
        assertEquals(card, result[0])
    }

    @Test
    fun getCardsTest_multipleCards() {
        val card1 = "Bash"
        val card2 = "Strike"
        val card3 = "Claw"
        val body = "[[$card1]], [[$card2]], and [[$card3]] are all examples of Attack cards"
        val result = getCards(body)
        assertEquals(3, result.size)
        assertEquals(card1, result[0])
        assertEquals(card2, result[1])
        assertEquals(card3, result[2])
    }

    @Test
    fun getCardsTest_multipleCards2() {
        val card1 = "A Thousand Cuts"
        val card2 = "Cloak and Dagger"
        val body = "[[$card1]] and [[$card2]]"
        val result = getCards(body)
        assertEquals(2, result.size)
    }

}