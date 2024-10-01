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
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = providers.gradleProperty(key).get()

fun fileProperties(key: String) = project.findProperty(key).toString().let { if (it.isNotEmpty()) file(it) else null }

fun environment(key: String) = providers.environmentVariable(key)

plugins {
  // Java support
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.gradleIntelliJPlugin)
  alias(libs.plugins.changelog)
  alias(libs.plugins.detekt)
  alias(libs.plugins.ktlint)
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginName: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

val javaVersion: String by project

group = pluginGroup
version = pluginVersion

val depsDartVersion: String = properties("depsDartVersion")
val depsGoVersion: String = properties("depsGoVersion")
val depsPhpVersion: String = properties("depsPhpVersion")
val depsPyVersion: String = properties("depsPyVersion")
val depsRubyVersion: String = properties("depsRubyVersion")
val depsScalaVersion: String = properties("depsScalaVersion")
val depsRVersion: String = properties("depsRVersion")
val depsRustVersion: String = properties("depsRustVersion")
val depsLuaVersion: String = properties("depsLuaVersion")
val depsVueVersion: String = properties("depsVueVersion")
val depsSvelteVersion: String = properties("depsSvelteVersion")

// Configure project's dependencies
repositories {
  mavenCentral()
  mavenLocal()
  gradlePluginPortal()

  intellijPlatform {
    defaultRepositories()
    jetbrainsRuntime()
  }
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
  implementation("commons-io:commons-io:2.11.0")
  implementation("com.thoughtworks.xstream:xstream:1.4.20")

  intellijPlatform {
    intellijIdeaUltimate(platformVersion, useInstaller = false)
    instrumentationTools()
    pluginVerifier()
    zipSigner()

    bundledPlugins(
      "com.intellij.java",
      "com.intellij.java-i18n",
      "com.intellij.database",
      "com.intellij.css",
      "com.intellij.properties",
      "org.jetbrains.plugins.yaml",
      "org.intellij.plugins.markdown",
      "org.jetbrains.kotlin",
    )

    plugins(
      "Dart:$depsDartVersion",
      "PythonCore:$depsPyVersion",
      "org.jetbrains.plugins.go:$depsGoVersion",
      "org.intellij.scala:$depsScalaVersion",
      "org.jetbrains.plugins.ruby:$depsRubyVersion",
      "com.jetbrains.php:$depsPhpVersion",
      "R4Intellij:$depsRVersion",
      "com.jetbrains.rust:$depsRustVersion",
      "com.tang:$depsLuaVersion",
      "dev.blachut.svelte.lang:$depsSvelteVersion",
      "org.jetbrains.plugins.vue:$depsVueVersion",
    )
  }
}

kotlin {
  jvmToolchain(17)
}

intellijPlatform {
  pluginConfiguration {
    id = pluginGroup
    name = pluginName
    version = pluginVersion

    ideaVersion {
      sinceBuild = pluginSinceBuild
      untilBuild = pluginUntilBuild
    }

    changeNotes = provider {
      with(changelog) {
        renderItem(
          (getOrNull(pluginVersion) ?: getUnreleased())
            .withHeader(false)
            .withEmptySections(false),
          Changelog.OutputType.HTML,
        )
      }
    }
  }

  publishing {
    token = environment("PUBLISH_TOKEN")
    channels = listOf(pluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
  }

  signing {
    certificateChain = environment("CERTIFICATE_CHAIN")
    privateKey = environment("PRIVATE_KEY")
    password = environment("PRIVATE_KEY_PASSWORD")
  }

}

changelog {
  path.set("${project.projectDir}/docs/CHANGELOG.md")
  version.set(pluginVersion)
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  unreleasedTerm.set("Changelog")
  groups.set(listOf("Features", "Fixes", "Other", "Bump"))
}

detekt {
  config.setFrom("./detekt-config.yml")
  buildUponDefaultConfig = true
  autoCorrect = true
}

tasks {
  javaVersion.let {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }

    withType<Detekt> {
      jvmTarget = it
      reports.xml.required.set(true)
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  buildSearchableOptions {
    enabled = false
  }

  register("markdownToHtml") {
    val input = File("./docs/CHANGELOG.md")
    File("./docs/CHANGELOG.html").run {
      writeText(markdownToHTML(input.readText()))
    }
  }
}
