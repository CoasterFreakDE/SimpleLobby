package de.coasterfreak.listener.servermenu

import fr.minuskube.inv.InventoryManager
import org.bukkit.plugin.java.JavaPlugin

class InvManager(plugin: JavaPlugin) : InventoryManager(plugin) {

    init {
        this.init()
    }
}