package de.coasterfreak.listener

import de.coasterfreak.extensions.chatCompatible
import de.coasterfreak.extensions.fromLegacy
import de.coasterfreak.extensions.toLegacy
import de.coasterfreak.utils.PermissionHook
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class ChatListener : Listener {

    val permission = Permission("simplelobby.chat.color", PermissionDefault.OP)

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player

        val chatContent = if (player.hasPermission(permission)) {
            event.message().chatCompatible()
        } else {
            event.message().toLegacy().replace("%", "﹪").fromLegacy().color(TextColor.color(223, 230, 233))
        }

        var userMessage = "#1abc9c«#2ecc71${player.name}#1abc9c» "

        var userComponent = Component.text("«").color(TextColor.fromHexString("#1abc9c"))
            .append(player.displayName().color(TextColor.fromHexString("#2ecc71")))
            .append(Component.text("» ").color(TextColor.fromHexString("#1abc9c")))

        if (PermissionHook.api != null) {
            val luckPerms = PermissionHook.api!!
            val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)
            val prefix = user.cachedData.metaData.prefix
            val suffix = user.cachedData.metaData.suffix


            userComponent = Component.text("«").color(TextColor.fromHexString("#1abc9c"))

            if (prefix != null) {
                userComponent = userComponent.append(Component.text("$prefix ${player.name}").chatCompatible())
            } else {
                userComponent =
                    userComponent.append(player.displayName())
            }

            if (suffix != null) {
                userComponent = userComponent.append(Component.text(" $suffix").chatCompatible())
            }

            userComponent = userComponent.append(Component.text("» ").color(TextColor.fromHexString("#1abc9c")))
        }

        event.renderer { source, display, message, _ ->
            userComponent
                .append(chatContent)
        }
    }

}