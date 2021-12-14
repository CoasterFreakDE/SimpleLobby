package de.coasterfreak.annotations

import org.bukkit.permissions.PermissionDefault

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegisterCommand(
    val name: String,
    val description: String = "",
    val permission: String = "",
    val aliases: Array<out String> = [],
    val usage: String = "",
    val permissionDefault : PermissionDefault = PermissionDefault.OP
)