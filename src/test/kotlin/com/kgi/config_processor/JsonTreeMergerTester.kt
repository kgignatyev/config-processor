package com.kgi.config_processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.Assert
import org.junit.Test

class JsonTreeMergerTester {
    @Test
    fun testMergeerAndExpressionResolution() {
        val om = ObjectMapper()
        val conf: ObjectNode = om.readTree("""
           { "a": {
              "b": "bb"
            },
            "c" : "dd",
            "e" : "${'$'}{a/b}"
           }
        """.trimIndent()) as ObjectNode

        val res = om.createObjectNode()

        JsonTreeMerger.merge( conf,res)
        Assert.assertEquals("bb", ExpressionResolver.resolveExpression("${'$'}{e}", conf))
        Assert.assertEquals("bb", ExpressionResolver.resolveExpression("${'$'}{e}", res))
    }

}
