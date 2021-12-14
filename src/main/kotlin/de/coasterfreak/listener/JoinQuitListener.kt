package de.coasterfreak.listener

import de.coasterfreak.Config
import de.coasterfreak.commands.BuildCommand
import de.coasterfreak.extensions.rgb
import de.coasterfreak.utils.ItemBuilder
import de.coasterfreak.utils.PermissionHook
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemFlag

class JoinQuitListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        with(event) {
            var username = player.displayName
            if (PermissionHook.api != null) {
                val luckPerms = PermissionHook.api!!
                val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)
                val prefix = user.cachedData.metaData.prefix
                val suffix = user.cachedData.metaData.suffix

                if (prefix != null) {
                    username = "$prefix $username"
                }

                if (suffix != null) {
                    username += " $suffix"
                }
            }


            joinMessage = "#1abc9c«#2ecc71&l${username}&r#1abc9c» <#16a085>hat den Server betreten!</#27ae60>".rgb()

            player.sendMessage(
                Config.getOrCreateDefault(
                    "messages.welcome",
                    " \n" +
                            "          #1abc9c» Willkommen auf <#686de0>DevSky.one</#4834d4>\n \n" +
                            "   #1abc9c» Bitte beachte die Regeln!\n" +
                            "   #1abc9c» Wenn du Hilfe brauchst, schreibe einfach /help\n" +
                            " "
                ).replace("%player%", player.displayName).rgb()
            )

            player.performCommand("spawn")
            player.foodLevel = 20
            player.health = 20.0

            player.inventory.setItem(8, ItemBuilder(Material.CHEST) {
                display("<#ffbe76>Server auswählen</#ff7979>")
                lore("§7Klicke um zu den Servern zu wechseln")
                flag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
                enchant(mapOf(Enchantment.DAMAGE_ALL to 1))
            }.build())
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        with(event) {
            var username = player.displayName
            if (PermissionHook.api != null) {
                val luckPerms = PermissionHook.api!!
                val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)
                val prefix = user.cachedData.metaData.prefix
                val suffix = user.cachedData.metaData.suffix

                if (prefix != null) {
                    username = "$prefix $username"
                }

                if (suffix != null) {
                    username += " $suffix"
                }
            }

            quitMessage =
                "#e67e22«#e67e22§l&l${username}&r§r#e67e22» <#c0392b>hat den Server verlassen!</#e74c3c>".rgb()


            if (player in BuildCommand.builders) {
                val (inventory, armor, extraContent) = BuildCommand.builders.remove(player) ?: return@with
                player.inventory.clear()

                player.inventory.storageContents = inventory
                player.inventory.setArmorContents(armor)
                player.inventory.setExtraContents(extraContent)
            }

        }
    }

}