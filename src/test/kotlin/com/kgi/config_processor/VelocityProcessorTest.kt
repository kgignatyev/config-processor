package com.kgi.config_processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.Assert
import org.junit.Test


class VelocityProcessorTest {

    @Test
    fun testVelocity(){
        val om = ObjectMapper()
        val conf: ObjectNode = om.readTree("""
           { "a": {
              "b": "bb"
            },
            "c" : "dd",
            "e" : "${'$'}{a/b}"
           }
        """.trimIndent()) as ObjectNode
        val res =  VelocityProcessor.processTemplate("some ${'$'}{cfg.get('a/b')}",conf )
        Assert.assertEquals("some bb",res)

    }
}
