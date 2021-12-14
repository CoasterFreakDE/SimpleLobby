package de.coasterfreak.commands

import de.coasterfreak.Config.config
import de.coasterfreak.Config.reloadConfig
import de.coasterfreak.annotations.RegisterCommand
import de.coasterfreak.extensions.prefixed
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "spawn",
    permission = "simplelobby.spawn",
    permissionDefault = PermissionDefault.TRUE,
    usage = "/spawn",
    description = "Teleportiert dich zum Spawn"
)
class SpawnCommand : CommandExecutor {


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("<#ff7979>Du musst ein Spieler sein!</#eb4d4b>".prefixed())
            return true
        }

        if (!config.contains("spawn")) {
            reloadConfig()
        }

        val spawn = config.getLocation("spawn")
        if (spawn == null) {
            sender.sendMessage("<#ff7979>Der Spawn wurde nicht gesetzt!</#eb4d4b>".prefixed())
            return true
        }

        sender.teleport(spawn)
        sender.sendMessage("<#badc58>Du wurdest zum Spawn teleportiert!</#6ab04c>".prefixed())
        return true
    }

}