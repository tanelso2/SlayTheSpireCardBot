package com.tanelso2.slaythespirecardbot.providers

import com.tanelso2.slaythespirecardbot.providers.WikiaProvider.getCardNameTitle
import org.junit.Assert.assertEquals
import org.junit.Test

class WikiaProviderTest {

    @Test
    fun getCardNameTitleTest() {
        val cases = mapOf(
                "Bash" to "Bash",
                "Piercing Wail" to "Piercing_Wail",
                "leg sweep" to "Leg_Sweep",
                "a thousand Cuts" to "A_Thousand_Cuts",
                "Dodge And Roll" to "Dodge_and_Roll"
        )
        cases.forEach { (input, expected) ->
            assertEquals(expected, getCardNameTitle(input))
        }
    }
}