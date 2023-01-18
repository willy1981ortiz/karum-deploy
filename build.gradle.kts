val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("nu.studer.jooq") version "7.1.1"
    id("com.google.cloud.tools.jib") version "3.3.0"
}

group = "com"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.jetty.EngineMain")

/*    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")*/
}

jib {
    to {
        //image = "ghcr.io/fleeksoft/karum-deploy/approva:alpha-1.1.27-6"
        image = "ghcr.io/uria1990/karum-deploy/approva:test"
    }
    container.workingDirectory = "/app"
    setAllowInsecureRegistries(true)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-jetty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
   /* implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")*/

    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-compression:$ktor_version")
    implementation("io.ktor:ktor-server-caching-headers:$ktor_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    // Dependencies Related to HTML and Gson Serialization
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    // Dependencies Related to Database
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("org.jooq:jooq")
    jooqGenerator("mysql:mysql-connector-java:8.0.30")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation(platform("com.google.cloud:libraries-bom:26.1.3"))

    implementation ("com.google.cloud:google-cloud-vision")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

jooq {
    //version.set("3.15.1")  // default (can be omitted)
    //edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // default (can be omitted)

    configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(false)  // default (can be omitted)

            jooqConfiguration.apply {
                //logging = Logging.WARN
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://127.0.0.1:3306?serverTimezone=UTC"
                    user = "root"
                    password = "root"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "karum_card_web_db"
                        includes = ".*"
                        isDateAsTimestamp = false
                    }
                    generate.apply {
                        isDaos = true
                        isPojos = true
                        isRelations = true
                        isPojosEqualsAndHashCode = true
                    }
                    target.apply {
                        packageName = "com"
                        //directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}