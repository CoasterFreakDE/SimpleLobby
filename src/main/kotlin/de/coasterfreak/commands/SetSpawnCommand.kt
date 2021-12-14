package de.coasterfreak.commands

import de.coasterfreak.Config.config
import de.coasterfreak.annotations.RegisterCommand
import de.coasterfreak.extensions.prefixed
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "setspawn",
    permission = "simplelobby.setspawn",
    permissionDefault = PermissionDefault.OP,
    usage = "/setspawn",
    description = "Setzt den Spawn der Lobby"
)
class SetSpawnCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("<#ff7979>Du musst ein Spieler sein!</#eb4d4b>".prefixed())
            return true
        }

        config.set("spawn", sender.location)
        config.saveConfig()

        sender.sendMessage("<#badc58>Der Spawn wurde gesetzt!</#6ab04c>".prefixed())
        return true
    }

}