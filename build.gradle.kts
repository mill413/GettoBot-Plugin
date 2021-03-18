plugins {
    val kotlinVersion = "1.4.30"
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
    maven {
        url =uri("https://maven.pkg.github.com/mzdluo123/silk4j")
        credentials {
            username = "Mill413"
            password = "51796880716efb0fd1f8584faec9c19003c5cc41"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")
    api("net.mamoe", "mirai-core", "2.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.21")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc:2.1.5.RELEASE")
    implementation ("mysql:mysql-connector-java:8.0.16")
    implementation("io.github.mzdluo123:silk4j:1.1-dev")
}
