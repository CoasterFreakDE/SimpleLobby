package de.coasterfreak.commands

import de.coasterfreak.annotations.RegisterCommand
import de.coasterfreak.extensions.prefixed
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "build",
    permission = "simplelobby.build",
    description = "Wechselt in den Baumodus",
    usage = "/build [name]",
    aliases = ["b"],
    permissionDefault = PermissionDefault.OP
)
class BuildCommand : CommandExecutor {

    companion object {
        val builders = mutableMapOf<Player, Triple<Array<ItemStack>, Array<ItemStack>, Array<ItemStack>>>()

        fun addBuilder(player: Player) {
            val inventory = player.inventory.storageContents
            val armor = player.inventory.armorContents
            val extraContent = player.inventory.extraContents

            builders[player] = Triple(inventory, armor, extraContent)

            player.inventory.clear()
            player.gameMode = GameMode.CREATIVE
            player.playSound(player.location, Sound.BLOCK_PISTON_EXTEND, 1f, 1f)
        }

        fun removeBuilder(player: Player) {
            player.inventory.clear()

            val (inventory, armor, extraContent) = builders[player]!!
            player.inventory.storageContents = inventory
            player.inventory.setArmorContents(armor)
            player.inventory.setExtraContents(extraContent)

            player.gameMode = GameMode.ADVENTURE
            builders.remove(player)
            player.playSound(player.location, Sound.BLOCK_PISTON_CONTRACT, 1f, 1f)
        }

        fun Player.isBuilder(): Boolean {
            return builders.containsKey(this)
        }
    }

    /**
     * Add yourself or other players to the builders list with your current inventory content
     * or remove them from the list if they are already in the list and give them their inventory back
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("<#ff7979>Du musst ein Spieler sein!</#eb4d4b>".prefixed())
            return true
        }

        if (builders.containsKey(sender)) {
            removeBuilder(sender)
            sender.sendMessage("<#ff7979>Du bist nun wieder in dem normalen Modus.</#eb4d4b>".prefixed())
        } else {
            addBuilder(sender)
            sender.sendMessage("<#badc58>Du bist nun im Baumodus.</#6ab04c>".prefixed())
        }

        return true
    }
}