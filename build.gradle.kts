plugins {
    base
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
}

allprojects {
    group = "com.ntwhitfi"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
    kapt("com.google.dagger:dagger-compiler:2.25.2")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
