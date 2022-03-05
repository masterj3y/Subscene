package github.masterj3y.subscene.regexer

import java.util.regex.Pattern

class Regexer(regex: Regex) {

    private val pattern = Pattern.compile(regex.pattern)

    fun findGroups(string: String?): List<String>? {

        val matcher = pattern.matcher(string ?: "")

        val result = mutableListOf<String>()

        while (matcher.find()) {
            result.add(matcher.group())
        }

        return result.ifEmpty { null }
    }

    fun extractGroupValues(string: String?): List<String>? =
        pattern.toRegex().matchEntire(string ?: "")?.groupValues
}