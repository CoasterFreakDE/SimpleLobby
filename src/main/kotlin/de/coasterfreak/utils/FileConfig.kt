package de.coasterfreak.utils

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class FileConfig(fileName: String) : YamlConfiguration() {
    private var seperator: String?
    private val path: String
    fun saveConfig() {
        try {
            save(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        seperator = System.getProperty("file.seperator") // TODO: 28.03.2021 Fruxz - Mmmhh, separator?
        if (seperator == null) {
            seperator = "/"
        }
        path = "config$seperator$fileName"
        val file = File(path)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            load(path)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }
}