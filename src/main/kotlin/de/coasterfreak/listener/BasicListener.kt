package de.coasterfreak.listener

import com.destroystokyo.paper.ParticleBuilder
import de.coasterfreak.SimpleLobby
import de.coasterfreak.commands.BuildCommand.Companion.isBuilder
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class BasicListener : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.inventory.itemInMainHand.type == Material.CHEST) {
                // Open Menu
                SimpleLobby.serverMenu.inventory.open(player)
            }
            return@with
        }

        if (action != Action.PHYSICAL || player.isSneaking) {
            return@with
        }

        if (clickedBlock?.type == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            player.velocity = player.location.direction.multiply(1.5).setY(2)
            player.playSound(player.location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1f)

            ParticleBuilder(Particle.BLOCK_CRACK)
                .data(Material.REDSTONE_BLOCK.createBlockData())
                .count(15 * 5)
                .location(player.location)
                .offset(.5, .75, .5)
                .extra(.01)
                .spawn()
        }
    }

    @EventHandler
    fun onItemMove(event: InventoryClickEvent) = with(event) {
        if (whoClicked is Player && !(whoClicked as Player).isBuilder()) {
            isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryMoveItemEvent) = with(event) {
        isCancelled = true

        if (source.holder is Player) {
            if ((source.holder as Player).isBuilder()) {
                isCancelled = false
            }
        }

        if (destination.holder is Player) {
            if ((destination.holder as Player).isBuilder()) {
                isCancelled = false
            }
        }
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
            return@with
        }
    }

    @EventHandler
    fun onMultiPlace(event: BlockMultiPlaceEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
            return@with
        }
    }

    @EventHandler
    fun onDestroy(event: BlockBreakEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
            return@with
        }
    }

    @EventHandler
    fun onBlockIgnite(event: BlockIgniteEvent) = with(event) {
        if (player?.isBuilder()?.not() == true) {
            isCancelled = true
            return@with
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) = with(event) {
        if (entity is Player) {
            if (!(entity as Player).isBuilder()) {
                isCancelled = true
                damage = 0.0
                return@with
            }
        }
    }

    @EventHandler
    fun onDamageByEntity(event: EntityDamageByEntityEvent) = with(event) {
        if (entity is Player) {
            if (!(entity as Player).isBuilder()) {
                isCancelled = true
                damage = 0.0
                return@with
            }
        }

        if (damager is Player) {
            if (!(damager as Player).isBuilder()) {
                isCancelled = true
                damage = 0.0
                return@with
            }
        }
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) = with(event) {
        if (entity is Player) {
            if (!(entity as Player).isBuilder()) {
                isCancelled = true
                foodLevel = 20
                return@with
            }
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
            return@with
        }
    }

    @EventHandler
    fun onItemPickup(event: PlayerAttemptPickupItemEvent) = with(event) {
        if (!player.isBuilder()) {
            isCancelled = true
            return@with
        }
    }
}