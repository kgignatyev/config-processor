package com.kgi.config_processor


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlin.system.exitProcess


object ExpressionResolver {

    val expRegex = "\\$[{]([^}]+)[}]".toRegex()


    
    fun resolveExpression( exp:String, doc: ObjectNode):String {
        var res = exp
        var match = expRegex.find( exp )
        if( match != null){
            val start = 0
            do{
                val outerGroup = match!!.groups[0]!!
                val end = outerGroup.range.last+1
                val prefix =  res.substring( start, outerGroup.range.start )
                val replacement = nodeValueOf(doc, match.groups[1]!!.value )
                val suffix = res.substring( end )
                res = prefix + replacement  + suffix
                match = expRegex.find( res )
            }while ( match != null)
        }
        return res
    }




    fun nodeValueOf(doc: ObjectNode, nodeAddress: String): String {
        val normalizedNodeAddress = nodeAddress.replace('.','/')
        val node = doc.at("/$normalizedNodeAddress")
        if( node is MissingNode) {
            System.err.println("${nodeAddress} cannot be resolved")
            exitProcess(1)
        }
        return textValueOfNode( node )
    }

    private fun textValueOfNode(node: JsonNode): String {
        return when {
            node.isBoolean -> {
                "${node.asBoolean()}"
            }
            node.isInt -> {
                "${node.asInt()}"
            }
            node.isDouble -> {
                "${node.asDouble()}"
            }
            else -> {
                node.textValue()
            }
        }
    }
}
