package de.coasterfreak.listener.servermenu

import de.coasterfreak.SimpleLobby
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.ByteArrayInputStream
import java.io.DataInputStream

class ServerOnlineCounter : PluginMessageListener {

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        val byteInput = ByteArrayInputStream(message)
        val inn = DataInputStream(byteInput)
        try {
            when (inn.readUTF()) {
                "PlayerCount" -> {
                    val serverName = inn.readUTF()
                    val playerCount: Int = inn.readInt()
                    SimpleLobby.serverMenu.playerCounts[serverName.lowercase()] = playerCount
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}