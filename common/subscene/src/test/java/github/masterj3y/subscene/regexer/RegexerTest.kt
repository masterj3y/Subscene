package github.masterj3y.subscene.regexer

import io.kotest.matchers.shouldBe
import junit.framework.TestCase

class RegexerTest : TestCase() {

    fun testFindGroups() {
        // TODO: write this test
    }

    fun testExtractGroupValues() {
        val regexer = Regexer("find _(\\w+)_ and _(\\w+)_".toRegex())
        val groups = regexer.extractGroupValues("find _this_ and _that_")
        groups?.size shouldBe 3
        groups?.get(1) shouldBe "this"
        groups?.get(2) shouldBe "that"
    }
}