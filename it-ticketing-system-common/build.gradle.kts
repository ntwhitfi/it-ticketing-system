plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.61")
    implementation("junit:junit:4.12")
    implementation("com.google.dagger:dagger:2.25.2")
    kapt("com.google.dagger:dagger-compiler:2.25.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
