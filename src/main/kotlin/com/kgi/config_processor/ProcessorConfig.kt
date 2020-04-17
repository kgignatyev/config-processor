package com.kgi.config_processor


class ProcessorConfig {

    var debug: Boolean = true

    var release: String = "v1"

    var configDirs: List<String> = mutableListOf("configs","src/test/resources/configs")

    var tasks: List<Pair<String,String>> = mutableListOf(Pair("src/test/resources/templates/values.yaml","target/values.yaml"))

}
