plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    maven ("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven ("https://oss.sonatype.org/content/groups/public/")
    maven ("https://jitpack.io")
    maven ("https://repo.codemc.org/repository/maven-public/")
    maven ("https://repo.maven.apache.org/maven2/")
}

dependencies {
    implementation("com.github.cryptomorin:XSeries:8.7.1")
    implementation("de.tr7zw:item-nbt-api:2.11.3")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

group = "com.dnyferguson"
version = "3.1.5"
description = "MineableSpawners"
java.sourceCompatibility = JavaVersion.VERSION_16

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    shadowJar {
        relocate("de.tr7zw.changeme.nbtapi", "com.dnyferguson.mineablespawners.nbtapi")
        relocate("com.cryptomorin.xseries", "com.dnyferguson.mineablespawners.xseries")
    }
    processResources {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }
    }

}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}