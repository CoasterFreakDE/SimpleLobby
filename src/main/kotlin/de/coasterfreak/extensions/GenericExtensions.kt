package de.coasterfreak.extensions

import de.coasterfreak.SimpleLobby
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.melion.rgbchat.api.RGBApi
import org.bukkit.ChatColor


fun <T> T.print(): T {
    println(this)
    return this
}

fun Array<BaseComponent>.print(): Array<BaseComponent> {
    this.forEach {
        println(it.toString())
    }
    return this
}

fun String.prefixed() = "${SimpleLobby.PREFIX}$this".rgb()
fun String.rgb() = ChatColor.translateAlternateColorCodes('&', RGBApi.toColoredMessage(this))
fun String.usage() = "<#badc58>CommandHandler</#f6e58d>#95afc0: Bitte benutze <#4834d4>$this</#686de0>".rgb()


infix fun TextComponent.plus(string: String) = this.append(
    LegacyComponentSerializer.legacyAmpersand().deserialize((string.rgb()))
)

infix fun TextComponent.plus(other: Component) = this.append(other)

fun Component.toLegacy() = LegacyComponentSerializer.legacyAmpersand().serialize(this)
fun String.fromLegacy() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)

fun String.toComponent() =
    BungeeComponentSerializer.legacy().deserialize(net.md_5.bungee.api.chat.TextComponent.fromLegacyText(this))

fun Component.chatCompatible() = this.toLegacy().rgb().replace("%", "﹪").toComponent()