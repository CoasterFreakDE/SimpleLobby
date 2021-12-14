package de.coasterfreak.extensions

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.soundExecution() {
    playSound(location, Sound.ENTITY_ITEM_PICKUP, .75F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_LEATHER, .25F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_CHAIN, .1F, 2F)
}

fun Player.playSound(loc: Location = this.location, sound: Sound, volume: Int, pitch: Int = 1) =
    this.playSound(loc, sound, volume.toFloat(), pitch.toFloat())

fun CommandSender.playSound(loc: Location = (this as Player).location, sound: Sound, volume: Int, pitch: Int) =
    (this as Player).playSound(loc, sound, volume.toFloat(), pitch.toFloat())

fun Player.addItem(vararg item: ItemStack) = this.inventory.addItem(*item)

fun <T> Player.playEffect(effect: Effect, data: T? = null) = playEffect(this.location, effect, data)