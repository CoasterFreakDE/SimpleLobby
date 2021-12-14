package de.coasterfreak.utils

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.melion.rgbchat.api.RGBApi
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.URI
import java.net.URISyntaxException
import java.util.*


class ItemBuilder(material: Material, count: Int = 1, dsl: ItemBuilder.() -> Unit = {}) {

    /**
     * The itemStack to get
     * equals to build()
     */
    var itemStack = ItemStack(material, count)

    init {
        dsl.invoke(this)
    }

    /**
     * Change the displayname of the item
     * @param displayName The new displayname (could be rgb)
     */
    fun display(displayName: String): ItemBuilder {
        val meta = itemStack.itemMeta
        var display = displayName
        try {
            display = RGBApi.toColoredMessage(display)
        } catch (_: ClassNotFoundException) {
        }
        display = ChatColor.translateAlternateColorCodes('&', display)
        meta.displayName(display)
        itemStack.itemMeta = meta
        return this
    }

    @FunctionalInterface
    fun interface Performer<T> {
        fun perform(itemBuilder: T): T
    }

    fun condition(condition: Boolean, consumer: Performer<ItemBuilder>): ItemBuilder {
        if (condition) {
            return consumer.perform(this)
        }
        return this
    }


    fun setOwner(uuid: UUID): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val skullMeta = itemStack.itemMeta as SkullMeta
        skullMeta.owningPlayer = Bukkit.getOfflinePlayer(uuid)
        itemStack.itemMeta = skullMeta
        return this
    }

    fun texture(texture: String): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val url = "http://textures.minecraft.net/texture/$texture"

        val skullMeta = itemStack.itemMeta as SkullMeta
        mutateItemMeta(skullMeta, urlToBase64(url))
        itemStack.itemMeta = skullMeta

        return this
    }


    /**
     * Change the displayname of the item if condition is true
     * @param displayName The new displayname (could be rgb)
     */
    fun displayIf(displayName: String, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return display(displayName)
        }
        return this
    }

    fun lore(vararg lores: String): ItemBuilder {
        val meta = itemStack.itemMeta
        val lore = mutableListOf<String>()

        lores.forEach {
            val lines = it.split("\n")
            for (lineIn in lines) {
                var line = lineIn
                try {
                    line = RGBApi.toColoredMessage(line)
                } catch (_: ClassNotFoundException) {
                }
                line = ChatColor.translateAlternateColorCodes('&', line)
                lore.add(line)
            }
        }
        meta.lore(lore.convert { this.adventureText().asComponent() }.toMutableList())
        itemStack.itemMeta = meta
        return this
    }

    fun loreIf(vararg lores: String, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return lore(*lores)
        }
        return this
    }

    /**
     * Add flags
     * @param flags
     */
    fun flag(vararg flags: ItemFlag): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.addItemFlags(*flags)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Add enchants
     * @param enchants
     */
    fun enchant(enchants: Map<Enchantment, Int>): ItemBuilder {
        val meta = itemStack.itemMeta
        enchants.forEach {
            meta.addEnchant(it.key, it.value, true)
        }
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Add enchants if condition is true
     * @param enchants
     * @param condition
     */
    fun enchantIf(enchants: Map<Enchantment, Int>, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return enchant(enchants)
        }
        return this
    }


    /**
     * Set material if condition is true
     * @param material
     * @param condition
     */
    fun type(material: Material): ItemBuilder {
        itemStack.type = material
        return this
    }

    /**
     * Set material if condition is true
     * @param material
     * @param condition
     */
    fun typeIf(material: Material, condition: Boolean = false): ItemBuilder {
        if (condition) {
            itemStack.type = material
        }
        return this
    }

    /**
     * The itemStack to get
     * equals to .itemStack
     */
    fun build(): ItemStack {
        return itemStack
    }

    companion object {
        fun fromItemStack(itemStack: ItemStack): ItemBuilder {
            val builder = ItemBuilder(itemStack.type)
            builder.itemStack = itemStack
            return builder
        }

        fun Any?.adventureText() = adventureLegacyText()

        private fun Any?.adventureLegacyText() =
            LegacyComponentSerializer.builder().hexColors().extractUrls().build()
                .deserializeOr("$this", Component.text("FAILED", NamedTextColor.RED))!!

        fun ItemMeta.displayName(name: String) = this.displayName(name.adventureText())

        infix fun <I, O> Collection<I>.convert(process: I.() -> O): Collection<O> {
            val out = mutableListOf<O>()
            forEach {
                out.add(process(it))
            }
            return out
        }

        // some reflection stuff to be used when setting a skull's profile
        private val blockProfileField: Field? = null
        private var metaSetProfileMethod: Method? = null
        private var metaProfileField: Field? = null

        private fun mutateItemMeta(meta: SkullMeta, b64: String) {
            try {
                if (metaSetProfileMethod == null) {
                    metaSetProfileMethod = meta.javaClass.getDeclaredMethod("setProfile", GameProfile::class.java)
                    metaSetProfileMethod!!.isAccessible = true
                }
                metaSetProfileMethod!!.invoke(meta, makeProfile(b64))
            } catch (ex: NoSuchMethodException) {
                // if in an older API where there is no setProfile method,
                // we set the profile field directly.
                try {
                    if (metaProfileField == null) {
                        metaProfileField = meta.javaClass.getDeclaredField("profile")
                        metaProfileField!!.isAccessible = true
                    }
                    metaProfileField!!.set(meta, makeProfile(b64))
                } catch (ex2: NoSuchFieldException) {
                    ex2.printStackTrace()
                } catch (ex2: IllegalAccessException) {
                    ex2.printStackTrace()
                }
            } catch (ex: IllegalAccessException) {
                try {
                    if (metaProfileField == null) {
                        metaProfileField = meta.javaClass.getDeclaredField("profile")
                        metaProfileField!!.isAccessible = true
                    }
                    metaProfileField!!.set(meta, makeProfile(b64))
                } catch (ex2: NoSuchFieldException) {
                    ex2.printStackTrace()
                } catch (ex2: IllegalAccessException) {
                    ex2.printStackTrace()
                }
            } catch (ex: InvocationTargetException) {
                try {
                    if (metaProfileField == null) {
                        metaProfileField = meta.javaClass.getDeclaredField("profile")
                        metaProfileField!!.isAccessible = true
                    }
                    metaProfileField!!.set(meta, makeProfile(b64))
                } catch (ex2: NoSuchFieldException) {
                    ex2.printStackTrace()
                } catch (ex2: IllegalAccessException) {
                    ex2.printStackTrace()
                }
            }
        }

        private fun makeProfile(b64: String): GameProfile {
            // random uuid based on the b64 string
            val id = UUID(
                b64.substring(b64.length - 20).hashCode().toLong(),
                b64.substring(b64.length - 10).hashCode().toLong()
            )
            val profile = GameProfile(id, "Player")
            profile.properties.put("textures", Property("textures", b64))
            return profile
        }

        private fun urlToBase64(url: String): String {
            val actualUrl: URI
            try {
                actualUrl = URI(url)
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
            val toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"$actualUrl\"}}}"
            return Base64.getEncoder().encodeToString(toEncode.toByteArray())
        }
    }
}