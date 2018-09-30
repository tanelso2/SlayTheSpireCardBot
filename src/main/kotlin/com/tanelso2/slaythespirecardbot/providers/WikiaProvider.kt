package com.tanelso2.slaythespirecardbot.providers

object WikiaProvider: Provider {
    private const val BASE_URL = "http://slay-the-spire.wikia.com/wiki/"

    override fun getMessage(cardName: String): String {
        return "[$cardName](${getFullUrl(cardName)})"
    }

    fun getFullUrl(cardName: String): String {
        // TODO: Verify this URL leads to an actual page
        return "${BASE_URL}${getCardNameTitle(cardName)}"
    }

    fun getCardNameTitle(cardName: String): String {
        val lowerCaseName = cardName.toLowerCase()
        val mapResult = oddballs[lowerCaseName]
        return mapResult ?:
                cardName
                .split(' ')
                .filter { it.isNotEmpty() }
                .map {"${it.substring(0, 1).toUpperCase()}${it.substring(1).toLowerCase()}" }
                .joinToString(separator = "_")

    }

    // Card names that don't follow the standard capitalization scheme
    // Or just shortcuts for commonly messed up names
    private val oddballs: Map<String, String> = mapOf(
            "auto shields" to "Auto-Shields",
            "auto-shields" to "Auto-Shields",
            "blood for blood" to "Blood_for_Blood",
            "creative ai" to "Creative_AI",
            "dodge and roll" to "Dodge_and_Roll",
            "flash of steel" to "Flash_of_Steel",
            "ftl" to "FTL",
            "go for the eyes" to "Go_for_the_Eyes",
            "hand of greed" to "Hand_of_Greed",
            "jax" to "J.A.X.",
            "master of strategy" to "Master_of_Strategy",
            "multi cast" to "Multi-Cast",
            "multi-cast" to "Multi-Cast",
            "multicast" to "Multi-Cast",
            "pot of greed" to "Hand_of_Greed",
            "rip and tear" to "Rip_and_Tear",
            "storm of steel" to "Storm_of_Steel",
            "tools of the trade" to "Tools_of_the_Trade",
            "turbo" to "TURBO",
            "war cry" to "Warcry",
            "well laid plans" to "Well-Laid_Plans",
            "well-laid plans" to "Well-Laid_Plans",
            "ring of the snake" to "Ring_of_the_Snake",
            "art of war" to "Art_of_War",
            "bag of marbles" to "Bag_of_Marbles",
            "bag of preparation" to "Bag_of_Preparation",
            "gold plated cables" to "Gold-Plated_Cables",
            "gold-plated cables" to "Gold-Plated_Cables",
            "meat on the bone" to "Meat_on_the_Bone",
            "self forming clay" to "Self-Forming_Clay",
            "bird faced urn" to "Bird-Faced_Urn",
            "bird-faced urn" to "Bird-Faced_Urn",
            "du vu doll" to "Du-Vu_Doll",
            "du-vu doll" to "Du-Vu_Doll",
            "thread and needle" to "Thread_and_Needle",
            "mark of pain" to "Mark_of_Pain",
            "ring of the serpent" to "Ring_of_the_Serpent",
            "face of cleric" to "Face_of_Cleric",
            "mark of the bloom" to "Mark_of_the_Bloom",
            "serpent head" to "Ssserpent_Head",
            "sling of courage" to "Sling_of_Courage"
    )
}