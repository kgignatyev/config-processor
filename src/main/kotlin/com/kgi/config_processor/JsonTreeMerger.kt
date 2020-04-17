package com.kgi.config_processor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import java.lang.Exception


object JsonTreeMerger {



    fun merge( from: JsonNode, toObject: ObjectNode):ObjectNode {
        walker(null, from, toObject, toObject)
        return toObject
    }

    private fun walker(nodename: String?, fromNode: JsonNode, resultNode:JsonNode, result: ObjectNode, debug:Boolean = false) {

        if (fromNode.isObject) {
            val iterator = fromNode.fields()
            val nodesList =  iterator.asSequence().toList()
            for (node in nodesList) {
                val name = node.key
                val fromNodeValue = node.value
                when ( fromNodeValue ){
                    is ObjectNode -> {
                        val destinationNode = resultNode.get(name)?:(resultNode as ObjectNode).putObject(name)
                        walker(name, fromNodeValue, destinationNode,result )
                    }
                    is ArrayNode -> {
                        val destinationNode = (resultNode as ObjectNode).arrayNode()
                        walker(name, fromNodeValue, destinationNode,result )
                    }
                    is TextNode -> {
                        if( debug)  println(" text Node: " + fromNodeValue.asText())
                        val resolvedVal = ExpressionResolver.resolveExpression( fromNodeValue.asText(), result )
                        setValue(resultNode, name, resolvedVal)
                    }
                    is IntNode, is DoubleNode, is BooleanNode-> {
                        if( debug)  println("  valueNode: " + fromNodeValue.asText())
                        val resolvedVal = ExpressionResolver.resolveExpression( fromNodeValue.asText(), result )
                        setValue(resultNode, name, resolvedVal)
                    }
                    else -> throw Exception("cannot insert $fromNodeValue object into: $resultNode")
                }

            }
        } else if (fromNode.isArray) {
            val arrayItemsIterator = fromNode.elements()
            val arrayItemsList = arrayItemsIterator.asSequence().toList()
            val newArrayNode = (resultNode as ArrayNode)
            for (arrayItem in arrayItemsList) {
                walker("array item", arrayItem,  newArrayNode,result)
            }
        } else {

                println("$nodename  node some other type")

        }
    }

    private fun setValue(resultNode: JsonNode, name: String?, v: String  ) {
        when (resultNode) {
            is ObjectNode -> resultNode.put(name!!, v)
            is ArrayNode -> resultNode.add(v)
        }
    }
}
