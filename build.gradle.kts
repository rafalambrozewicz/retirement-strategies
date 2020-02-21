plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.61")
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.3.61")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "app.demo.retirement.strategies.AppKt"
}
