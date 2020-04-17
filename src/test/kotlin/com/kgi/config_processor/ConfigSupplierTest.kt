package com.kgi.config_processor

import org.junit.Test

/**
 *
 */


class ConfigSupplierTest {

    @Test
    fun testConfigLoad(){

        val processorCfg = ProcessorConfig()
        processorCfg.configDirs = listOf("src/test/resources/configs")
        val configSupplier = ConfigSupplier(ConfigProcessor.yamlMapper, processorCfg)
        val cfg = configSupplier.effectiveConfig
        ConfigProcessor.yamlMapper.writerWithDefaultPrettyPrinter().writeValue( System.out, cfg)
    }
}
