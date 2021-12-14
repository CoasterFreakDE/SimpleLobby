package de.coasterfreak.utils

import de.coasterfreak.utils.ConsoleColors.errorMessage
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit


object PermissionHook {

    var api: LuckPerms? = null

    fun registerHook() {
        try {
            val provider = Bukkit.getServicesManager().getRegistration(
                LuckPerms::class.java
            )
            if (provider != null) {
                api = provider.provider
            } else {
                errorMessage("LuckPerms wurde nicht gefunden! Es werden keine Ränge und Farben verwendet!")
            }
        } catch (exception: ClassNotFoundException) {
            errorMessage("LuckPerms wurde nicht gefunden! Es werden keine Ränge und Farben verwendet!")
        } catch (exception: NoClassDefFoundError) {
            errorMessage("LuckPerms wurde nicht gefunden! Es werden keine Ränge und Farben verwendet!")
        }
    }
}