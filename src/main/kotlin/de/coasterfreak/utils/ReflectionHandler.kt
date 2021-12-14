package de.coasterfreak.utils

import de.coasterfreak.annotations.RegisterCommand
import de.coasterfreak.extensions.rgb
import de.coasterfreak.extensions.usage
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.event.Listener
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.reflections8.Reflections

class ReflectionHandler(private val plugin: JavaPlugin) {

    init {
        registerCommands()
        registerListener()
    }

    private fun registerCommands() {
        val reflections = Reflections("de.coasterfreak.commands")

        for (clazz in reflections.getTypesAnnotatedWith(RegisterCommand::class.java)) {
            try {
                val annotation: RegisterCommand = clazz.getAnnotation(RegisterCommand::class.java)

                val pluginClass: Class<PluginCommand> = PluginCommand::class.java
                val constructor = pluginClass.getDeclaredConstructor(String::class.java, Plugin::class.java)

                constructor.isAccessible = true

                val command: PluginCommand = constructor.newInstance(annotation.name, plugin)

                Permission(annotation.permission, annotation.permissionDefault).apply {
                    Bukkit.getPluginManager().addPermission(this)
                }

                command.aliases = annotation.aliases.toList()
                command.description = annotation.description
                command.permission = annotation.permission
                command.usage = annotation.usage.usage()

                command.setExecutor(clazz.getDeclaredConstructor().newInstance() as CommandExecutor)

                Bukkit.getCommandMap().register("simplelobby", command)
                Bukkit.getConsoleSender().sendMessage("#2ecc71Command ${command.name} registiert".rgb())

            } catch (exception: InstantiationError) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
    }

    private fun registerListener() {
        val reflections = Reflections("de.coasterfreak.listener")

        for (clazz in reflections.getSubTypesOf(Listener::class.java)) {
            try {
                val constructor = clazz.getDeclaredConstructor()

                constructor.isAccessible = true

                val event = constructor.newInstance() as Listener

                Bukkit.getPluginManager().registerEvents(event, plugin)
                Bukkit.getConsoleSender()
                    .sendMessage("#9b59b6Listener ${event.javaClass.simpleName} registiert".rgb())
            } catch (exception: InstantiationError) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
    }

}