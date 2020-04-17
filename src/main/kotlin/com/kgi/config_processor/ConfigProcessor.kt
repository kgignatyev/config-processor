package com.kgi.config_processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.apache.commons.cli.*
import java.io.File
import kotlin.system.exitProcess


object ConfigProcessor {


        val yamlMapper = ObjectMapper(YAMLFactory())


        val jsonMapper = ObjectMapper()


        @JvmStatic
        fun main(args: Array<String>) {
            val processorCfg = ProcessorConfig()
            val parser: CommandLineParser = DefaultParser()
            val options = Options()
            options.addOption( "r", "release", true, "release name, if missed ")
            options.addOption( "t", "task", true, "pairs of sources and destinations like some.src:out1.txt")
            options.addOption( "c", "config", true, "sources of configurations (directory of yaml or json files)")
            options.addOption( "h", "help", false, "help info")
            val cmd: CommandLine = parser.parse(options, args)

            if( cmd.hasOption("h")){
                val formatter = HelpFormatter()
                formatter.printHelp("config-processor", options)
                exitProcess(0)
            }
            if( cmd.hasOption("r")){
                processorCfg.release = cmd.getOptionValue("release")
            }
            if( cmd.hasOption("t")) {
                val tasks = cmd.getOptionValues("task")
                processorCfg.tasks = tasks.map {
                    val parts = it.split(":",";")
                    Pair(parts[0],parts[1])
                }.toMutableList()
            }
            if( cmd.hasOption("c")) {
                val configs = cmd.getOptionValues("config")
                processorCfg.configDirs = configs.toMutableList()
            }
            val configSupplier = ConfigSupplier( yamlMapper, processorCfg)
            val config = configSupplier.effectiveConfig
            val treeResolver = ConfigFilesProcessor(yamlMapper, jsonMapper, config, processorCfg)
            processorCfg.tasks.forEach {
                val inFile = File(it.first)
                val outFile = File(it.second)
                val message = "processing template ${inFile.absolutePath} \n\tto ${outFile.absolutePath}"
                println(message)
                treeResolver.processFileTo(inFile, outFile)

            }
        }

}
