import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.2.70"
}

group = "com.tanelso2.slaythespirecardbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4.1")
    implementation("net.dean.jraw:JRAW:1.1.0")
    implementation("org.jooq:jooq-codegen:3.11.5")
    implementation("org.jooq:jooq-meta:3.11.5")
    implementation("org.jooq:jooq:3.11.5")
    implementation("org.postgresql:postgresql:42.2.5")
    testCompile("junit", "junit", "4.12")
    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}