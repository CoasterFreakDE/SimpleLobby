package de.coasterfreak

import de.coasterfreak.utils.FileConfig

object Config {

    var config = FileConfig("config.yml")

    fun reloadConfig() {
        config = FileConfig("config.yml")
    }

    fun getOrCreateDefault(path: String, default: String): String {
        val value = config.getString(path)
        if (value == null) {
            config.set(path, default)
            config.saveConfig()
            return default
        }
        return value
    }
    
}