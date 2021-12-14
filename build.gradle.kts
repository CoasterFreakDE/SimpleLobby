import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

group = "cloud.melion"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.pl3x.net/")
    maven("https://jitpack.io")
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:bukkit:1.18-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("com.mojang:authlib:1.5.25")

    implementation("com.github.F1b3rDEV", "minecraft-spigot-rgb-chat-support", "1.0.5")
    implementation("net.oneandone.reflections8", "reflections8", "0.11.5")
    implementation("net.kyori", "adventure-text-serializer-bungeecord", "4.0.0")
    implementation("fr.minuskube.inv:smart-invs:1.2.7")
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}