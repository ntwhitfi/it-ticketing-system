plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

kapt {
    generateStubs = true
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":it-ticketing-system-common"))
    implementation("com.amazonaws:aws-lambda-java-core:1.2.0")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:1.11.692")
    implementation("joda-time:joda-time:2.10")
    implementation("org.passay:passay:1.4.0")
    implementation("io.github.microutils:kotlin-logging:1.7.7")
    implementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.61")
    implementation("junit:junit:4.12")
    implementation("javax.annotation:javax.annotation-api:1.2")
    implementation("com.google.dagger:dagger:2.25.2")
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