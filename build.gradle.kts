/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2023 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

fun fileProperties(key: String) = project.findProperty(key).toString().let { if (it.isNotEmpty()) file(it) else null }

fun environment(key: String) = providers.environmentVariable(key)

plugins {
  // Java support
  id("java")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
  id("org.jetbrains.intellij") version "1.15.0"
  // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
  id("org.jetbrains.changelog") version "2.1.2"
  // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
  id("io.gitlab.arturbosch.detekt") version "1.23.0"
  // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
  id("org.jlleitschuh.gradle.ktlint") version "11.4.2"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

val depsDartVersion: String = properties("depsDartVersion")
val depsGoVersion: String = properties("depsGoVersion")
val depsPhpVersion: String = properties("depsPhpVersion")
val depsPyVersion: String = properties("depsPyVersion")
val depsRubyVersion: String = properties("depsRubyVersion")
val depsScalaVersion: String = properties("depsScalaVersion")
val depsRVersion: String = properties("depsRVersion")

// Configure project's dependencies
repositories {
  mavenCentral()
  maven(url = "https://maven-central.storage-download.googleapis.com/repos/central/data/")
  maven(url = "https://maven.aliyun.com/nexus/content/groups/public/")
  maven(url = "https://repo.eclipse.org/content/groups/releases/")
  maven(url = "https://www.jetbrains.com/intellij-repository/releases")
  maven(url = "https://www.jetbrains.com/intellij-repository/snapshots")
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
  implementation("com.jgoodies:jgoodies-forms:1.9.0")
  implementation("com.thoughtworks.xstream:xstream:1.4.20")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
  pluginName.set(properties("pluginName"))
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))
  downloadSources.set(true)
  instrumentCode.set(true)
  updateSinceUntilBuild.set(true)
//  localPath.set(properties("idePath"))

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins.set(listOf(
    "com.intellij.java",
    "com.intellij.java-i18n",
    "com.intellij.database",
    "com.intellij.css",
    "com.intellij.properties",
    "org.jetbrains.plugins.yaml",
    "org.intellij.plugins.markdown",
    "org.jetbrains.kotlin",
    "Dart:$depsDartVersion",
    "Pythonid:$depsPyVersion",
    "org.jetbrains.plugins.go:$depsGoVersion",
    "org.intellij.scala:$depsScalaVersion",
    "org.jetbrains.plugins.ruby:$depsRubyVersion",
    "com.jetbrains.php:$depsPhpVersion",
    "R4Intellij:$depsRVersion"
  ))
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
  path.set("${project.projectDir}/docs/CHANGELOG.md")
  version.set(properties("pluginVersion"))
  header.set(provider { version.get() })
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  unreleasedTerm.set("[Unreleased]")
  groups.set(listOf("Features", "Fixes", "Other", "Bump"))
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
  config = files("./detekt-config.yml")
  buildUponDefaultConfig = true
  autoCorrect = true
}

tasks {
  properties("javaVersion").let {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = it
      kotlinOptions.freeCompilerArgs += listOf("-Xskip-prerelease-check")
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  withType<Detekt> {
    jvmTarget = properties("javaVersion")
    reports.xml.required.set(true)
  }

  withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }

  sourceSets {
    main {
      java.srcDirs("src/main/java")
      resources.srcDirs("src/main/resources")
    }
  }

  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    // Get the latest available change notes from the changelog file
    changeNotes.set(changelog.getLatest().toHTML())
  }

  runPluginVerifier {
    ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map { it.trim() }.toList())
  }

  buildSearchableOptions {
    enabled = false
  }

  publishPlugin {
//    dependsOn("patchChangelog")
    token.set(System.getenv("INTELLIJ_PUBLISH_TOKEN") ?: file("./publishToken").readText().trim())
    channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  runIde {
//    ideDir.set(fileProperties("idePath"))
  }

  register("markdownToHtml") {
    val input = File("./docs/CHANGELOG.md")
    File("./docs/CHANGELOG.html").run {
      writeText(markdownToHTML(input.readText()))
    }
  }
}
