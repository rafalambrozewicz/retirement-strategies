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
    implementation("org.knowm.xchart:xchart:3.6.1")
    implementation("org.apache.commons:commons-math3:3.6.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "app.demo.retirement.strategies.AppKt"
}

sourceSets {
    named("main") {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDirs("src/main/kotlin", "src/main/kotlinX")
        }
    }
}
