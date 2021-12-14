package de.coasterfreak

import de.coasterfreak.Config.getOrCreateDefault
import de.coasterfreak.listener.servermenu.InvManager
import de.coasterfreak.listener.servermenu.ServerMenu
import de.coasterfreak.listener.servermenu.ServerOnlineCounter
import de.coasterfreak.utils.PermissionHook
import de.coasterfreak.utils.ReflectionHandler
import org.bukkit.plugin.java.JavaPlugin

class SimpleLobby : JavaPlugin() {


    /*
     *          Todos:
     *
     *      - Spawn Befehl (mit Setspawn)  👍
     *      - Kein Hunger 👍
     *      - Kein Schaden 👍
     *      - Nicht abbauen 👍
     *      - Keine Items droppen 👍
     *      - Menü zum auswählen von Servern (Konfigurierbar)
     *      - Join / Leave / Chat Messages mit RGB Support 👍
     *
     *          Extras:
     *
     *      - Jumppads 👍
     *      - Secrets ?
     *      - Parcours ?
     *
     */

    companion object {
        var PREFIX: String = getOrCreateDefault("prefix", "<#badc58>SimpleLobby</#f6e58d>#95afc0: ")
        lateinit var instance: SimpleLobby
        lateinit var serverMenu: ServerMenu
    }

    init {
        instance = this
    }

    override fun onEnable() {
        // Plugin startup logic
        ReflectionHandler(this)

        // Register Permission Hook for LuckPerms
        PermissionHook.registerHook()

        // Setup BungeeBride
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        server.messenger.registerIncomingPluginChannel(this, "BungeeCord", ServerOnlineCounter())

        //Register Menu
        val inventoryManager = InvManager(this)
        serverMenu = ServerMenu(inventoryManager)

        println("SimpleLobby is enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

}