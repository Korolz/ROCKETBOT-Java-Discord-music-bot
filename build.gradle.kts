plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "me.korolz"
version = "1.0.4"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.topi.wtf/releases") }
    maven { url = uri("https://maven.lavalink.dev/releases") }
}

val lavaplayerVersion = System.getenv("LAVAPLAYER_VERSION")
val ytsourceVersion = System.getenv("YTSOURCE_VERSION")
val lavasrcVersion = System.getenv("LAVASRC_VERSION")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")

    implementation("net.dv8tion:JDA:5.0.0-beta.20")

    implementation("dev.arbjerg:lavaplayer:$lavaplayerVersion")
    implementation("dev.lavalink.youtube:v2:$ytsourceVersion")
    implementation("com.github.topi314.lavasrc:lavasrc:$lavasrcVersion")
    implementation("com.github.topi314.lavasrc:lavasrc-protocol:$lavasrcVersion")

    implementation ("com.google.api-client:google-api-client:1.35.2")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.35.0")
    implementation ("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}