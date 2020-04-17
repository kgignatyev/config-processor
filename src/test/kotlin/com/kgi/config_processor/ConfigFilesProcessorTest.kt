package com.kgi.config_processor

import org.junit.Before
import org.junit.Test
import java.io.File


class ConfigFilesProcessorTest {

     lateinit var configFilesProcessor:ConfigFilesProcessor

    @Before
    fun setup(){
        val processorCfg = ProcessorConfig()
        val configSupplier = ConfigSupplier(ConfigProcessor.yamlMapper, processorCfg)
        configFilesProcessor = ConfigFilesProcessor(ConfigProcessor.yamlMapper, ConfigProcessor.jsonMapper, configSupplier.effectiveConfig, processorCfg)

    }

    @Test
    fun testYamlReading() {
        configFilesProcessor.processTreeLikeFile(File("src/test/resources/templates/values.yaml"))
    }

    @Test
    fun testTextReading() {
       println( configFilesProcessor.processAsVelocity( File("src/test/resources/templates/some.sql").readText()))
    }
}
