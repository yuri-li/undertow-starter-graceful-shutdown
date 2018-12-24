import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.gradle.plugins.ide.idea.model.Module

plugins {
    val kotlinV = "1.3.11"
    val ideaExtV = "0.5"

    id("org.jetbrains.kotlin.plugin.spring").version(kotlinV)
    id("org.jetbrains.kotlin.plugin.allopen").version(kotlinV)
    id("org.jetbrains.gradle.plugin.idea-ext").version(ideaExtV)
    `maven-publish`
    id("org.jetbrains.kotlin.jvm").version(kotlinV)
}

group = "org.undertowx"
version = "1.0-SNAPSHOT"

idea {
    module {
        setDownloadJavadoc(false)
        setDownloadSources(true)
    }
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.springframework.boot:spring-boot-starter-actuator:2.1.1.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-web:2.1.1.RELEASE"){
        exclude(module= "spring-boot-starter-tomcat")
    }
    compile("org.springframework.boot:spring-boot-starter-undertow:2.1.1.RELEASE")
    compile("com.ecwid.consul:consul-api:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0-alpha")
}

tasks.withType<Wrapper> {
    gradleVersion = "4.10"
    distributionType = Wrapper.DistributionType.ALL
}
tasks.withType<KotlinJvmCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
/*            groupId = this.groupId
            artifactId = this.artifactId
            version = this.version*/

            from(components["java"])
        }
    }
}