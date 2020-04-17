package com.kgi.config_processor


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 */
class ConfigSupplier(val yamlMapper: ObjectMapper, val processorConfig: ProcessorConfig) {


    var directories: List<File> = ArrayList()

    lateinit var effectiveConfig: ObjectNode

    init {
        readConfigFromDirectories(processorConfig.configDirs.map { File(it) })
    }


    fun readConfigFromDirectories(directories: List<File>) {
        this.directories = directories
        effectiveConfig = yamlMapper.createObjectNode()
        effectiveConfig.put("release", processorConfig.release)
        directories.forEach {
            readConfigFiles(it, effectiveConfig)
        }

    }

    private fun readConfigFiles(f: File, res: ObjectNode) {
        if (f.isDirectory) {
            val listOfFiles = f.listFiles() ?: arrayOf()
            listOfFiles.forEach { readConfigFiles(it, res) }
        } else {
            readConfigFromFile(f, res)
        }
    }

    private fun readConfigFromFile(f: File, res: ObjectNode) {
        when (f.extension) {
            "yaml", "yml" -> readFromYaml(f, res)
            "json" -> readFromJson(f, res)
            "properties" -> readProperties(f, res)
            else -> println("Ignored file: ${f.absolutePath}")
        }

    }



    private fun readFromYaml(f: File, result: ObjectNode) {
        if (processorConfig.debug) {
            println("reading config from file: ${f.absolutePath}")
        }
        val updates = yamlMapper.readTree(f)
        JsonTreeMerger.merge( updates, result)
    }

    private fun readFromJson(f: File, result: ObjectNode) {
        if (processorConfig.debug) {
            println("reading config from file: ${f.absolutePath}")
        }
        val updates = yamlMapper.readTree(f)
        JsonTreeMerger.merge( updates, result)
    }

    private fun readProperties(f: File, res: ObjectNode) {
        if (processorConfig.debug) {
            println("reading config from file: ${f.absolutePath}")
        }
        val props = Properties()
        props.load(FileReader(f))
        props.toList().forEach {
            updateFromProperty(res, it.first.toString(), it.second)
        }
    }

    private fun updateFromProperty(res: ObjectNode, key: String, value: Any?) {
        val keyParts = key.split('.')
        val objectNames = keyParts.subList(0, keyParts.size - 1)
        val fieldName = keyParts.get(keyParts.size - 1)
        var currentObject = res
        objectNames.forEach {
            var newCurrent = currentObject.get(it)
            if (newCurrent !is ObjectNode) {
                newCurrent = currentObject.putObject(it)
            }
            currentObject = newCurrent as ObjectNode
        }
        currentObject.put(fieldName, convertVal(value))
    }

    private fun convertVal(value: Any?): String? {
        return ExpressionResolver.resolveExpression( value.toString(), effectiveConfig )
    }
}
