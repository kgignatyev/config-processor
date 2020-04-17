package com.kgi.config_processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.Assert

import org.junit.Test


class ExpressionResolverTest {

    @Test
    fun testExpressionResolution(){
        val  om = ObjectMapper()
        val conf: ObjectNode = om.readTree("""
           { "a": {
              "b": "bb"
            },
            "c" : "dd",
            "e" : 42
           }
        """.trimIndent()) as ObjectNode

        Assert.assertEquals("bb", ExpressionResolver.resolveExpression("${'$'}{a/b}",conf))
        Assert.assertEquals("42", ExpressionResolver.resolveExpression("${'$'}{e}",conf))
    }
}
