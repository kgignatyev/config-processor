package com.kgi.config_processor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.File


class ConfigFilesProcessor (val yamlMapper:ObjectMapper, val jsonMapper:ObjectMapper,
                            val config: ObjectNode,
                            val processorConfig: ProcessorConfig
                    ){


    fun processFileTo(f: File,out:File) {
        when(f.extension){
                   "yaml","yml" -> yamlMapper.writer().writeValue(  out, processTreeLikeFile(f) )
                   "json" -> jsonMapper.writer().writeValue(  out, processTreeLikeFile(f) )
                    else -> out.writeText( processAsVelocity( f.readText()))
               }
    }

    fun processAsVelocity(content: String): String {
        return VelocityProcessor.processTemplate( content, config )
    }

    fun processTreeLikeFile(f: File):ObjectNode{
        val jsonNode = yamlMapper.readTree(f)
        JsonTreeMerger.merge( jsonNode,config)
        return processTree( jsonNode as ObjectNode )
    }





    fun processTree(root: ObjectNode):ObjectNode {
        walker(null, root, root, config)
        if( processorConfig.debug) println( yamlMapper.writer().writeValueAsString( root ) )
        return root
    }

    private fun walker(nodename: String?, node: JsonNode, parentNode:JsonNode, root:ObjectNode) {
        val nameToPrint = nodename ?: "must_be_root"
//        println("walker - node name: $nameToPrint")
        if (node.isObject) {
            val iterator = node.fields()

            val nodesList =  iterator.asSequence().toList()
//            if( processorConfig.debug)   println("Walk Tree - root:$node, elements keys:$nodesList")
            for (nodEntry in nodesList) {
                val name = nodEntry.key
                val newNode = nodEntry.value
                // System.out.println("  entry - key: " + name + ", value:" + node);
                walker(name, newNode, node,root )
            }
        } else if (node.isArray) {
            val arrayItemsIterator = node.elements()
            val arrayItemsList = arrayItemsIterator.asSequence().toList()
            for (arrayNode in arrayItemsList) {
                walker("array item", arrayNode, node, root)
            }
        } else {
            if (node.isValueNode) {
                if( processorConfig.debug)  println("  valueNode: " + node.asText())
                val resolvedVal = ExpressionResolver.resolveExpression( node.asText(), root)
                (parentNode as ObjectNode).put( nodename!!, resolvedVal)
            } else {
                println("$nameToPrint  node some other type")
            }
        }
    }
}
