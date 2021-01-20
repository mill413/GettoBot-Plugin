plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.0.0"
}

group = "top.harumill"
version = "0.1.1"

repositories {
    maven { url =uri("https://mirrors.huaweicloud.com/repository/maven") }
    maven { url =uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")}
    maven { url =uri("https://dl.bintray.com/kotlin/kotlin-eap")}
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")
    api("net.mamoe", "mirai-core", "2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.21")
}
