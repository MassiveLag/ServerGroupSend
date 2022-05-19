plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.6.10"
}


group = "com.johanneshq"
version = "1.1-BETA"

repositories {
    mavenCentral()

    maven (url = "https://papermc.io/repo/repository/maven-public/")
    maven (url = "https://repo.networkmanager.xyz/repository/maven-public/")
    maven (url = "https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib"))

    compileOnly("com.google.guava:guava:31.0.1-jre")
    compileOnly("org.jetbrains:annotations:16.0.2")
    compileOnly("com.github.Carleslc:Simple-YAML:1.7.2")

    //Depencies
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")
    compileOnly("nl.chimpgamer.networkmanager:api:2.11.1")

    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
    compileOnly("com.velocitypowered:velocity-api:3.1.0")

}

tasks {
    shadowJar {
        minimize {
            enabled = true
        }
        archiveFileName.set("${project.name}-v${project.version}.jar")
        destinationDirectory.set(file("$rootDir/output"))

        val shadedPackage = "nl.chimpgamer.networkmanager.shaded"
        val libPackage = "nl.chimpgamer.networkmanager.lib"

        //relocate("net.kyori", "$shadedPackage.kyori")
        relocate("kotlin", "$libPackage.kotlin")
        relocate("org.simpleyaml", "$libPackage.simpleyaml")
        relocate("net.kyori", "$shadedPackage.kyori")

    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}