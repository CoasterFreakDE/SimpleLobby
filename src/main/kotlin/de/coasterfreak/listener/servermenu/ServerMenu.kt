package de.coasterfreak.listener.servermenu

import com.google.common.io.ByteStreams
import de.coasterfreak.Config
import de.coasterfreak.SimpleLobby
import de.coasterfreak.extensions.prefixed
import de.coasterfreak.extensions.rgb
import de.coasterfreak.utils.ItemBuilder
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.InventoryManager
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import org.bukkit.entity.Player

class ServerMenu(inventoryManager: InventoryManager) : InventoryProvider {

    val playerCounts = mutableMapOf<String, Int>()

    val inventory: SmartInventory = SmartInventory.builder()
        .id("server_menu")
        .provider(this)
        .manager(inventoryManager)
        .size(6, 9)
        .title("<#706fd3>Wähle einen Server</#227093>".rgb())
        .build()

    private val blackPlaceholder = ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).display(" ").itemStack
    private val bluePlaceholder = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).display(" ").itemStack
    private val blackSlots = arrayOf(0..9, (9..53 step 9))
    private val blueSlots = arrayOf(10..17, 46..53, (19..37 step 9), (26..44 step 9))

    init {
        // Init Config if not exists

        if (!Config.config.contains("server-menu")) {
            val section = Config.config.createSection("server-menu.static-slots")

            blackSlots.forEach { slotProgression ->
                slotProgression.forEach { slot ->
                    section.set("slot_$slot", blackPlaceholder)
                }
            }
            blueSlots.forEach { slotProgression ->
                slotProgression.forEach { slot ->
                    section.set("slot_$slot", bluePlaceholder)
                }
            }

            val serverSection = Config.config.createSection("server-menu.server-slots")

            val survivalServer = serverSection.createSection("slot_31")
            survivalServer.set("server-name", "Survival")
            survivalServer.set("max-players", 64)
            survivalServer.set("item", ItemBuilder(Material.PLAYER_HEAD) {
                texture("1289d5b178626ea23d0b0c3d2df5c085e8375056bf685b5ed5bb477fe8472d94")
                display("<#badc58>Survival</#f9ca24>")
                lore("#686de0Es spielen gerade #badc58%playerCount%/%maxPlayers% #686de0Spieler.")
            }.itemStack)



            Config.config.saveConfig()
        }
    }

    override fun init(player: Player, contents: InventoryContents) {
        val staticItems = Config.config.getConfigurationSection("server-menu.static-slots")

        for (slotName in staticItems!!.getKeys(false)) {
            val slot = slotName.replace("slot_", "").toInt()
            contents.set(SlotPos(slot / 9, slot % 9), ClickableItem.empty(staticItems.getItemStack(slotName)))
        }
    }

    override fun update(player: Player, contents: InventoryContents) {
        val serverItems = Config.config.getConfigurationSection("server-menu.server-slots")

        for (slotName in serverItems!!.getKeys(false)) {
            val slot = slotName.replace("slot_", "").toInt()

            val item = serverItems.getItemStack("${slotName}.item") ?: continue
            val serverName = serverItems.getString("${slotName}.server-name") ?: continue

            val out = ByteStreams.newDataOutput()
            out.writeUTF("PlayerCount")
            out.writeUTF(serverName)
            player.sendPluginMessage(SimpleLobby.instance, "BungeeCord", out.toByteArray())

            val maxPlayers = serverItems.getInt("${slotName}.max-players")
            val playerCount = playerCounts[serverName.lowercase()] ?: 0

            // Replace lore placeholder
            val itemMeta = item.itemMeta
            val lore = itemMeta.lore
            itemMeta.lore = lore?.map { line ->
                line.replace(Regex("\\d/"), "%playerCount%/").replace("%playerCount%", playerCount.toString())
                    .replace("%maxPlayers%", maxPlayers.toString())
            }
            item.itemMeta = itemMeta

            contents.set(SlotPos(slot / 9, slot % 9), ClickableItem.of(item) {
                player.sendMessage("<#badc58>Du wirst auf den Server</#6ab04c> #f9ca24$serverName <#6ab04c>weitergeleitet.</#badc58>".prefixed())
                player.closeInventory()

                val out = ByteStreams.newDataOutput()
                out.writeUTF("Connect")
                out.writeUTF(serverName)
                player.sendPluginMessage(SimpleLobby.instance, "BungeeCord", out.toByteArray())
            })
        }
    }

}