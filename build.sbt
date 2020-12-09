import com.softwaremill.Publish
import sbt.{addSbtPlugin, _}
import Keys._
import sbtsoftwaremill.BuildInfo

val commonSettings = Publish.ossPublishSettings ++ Seq(
  scalaVersion := "2.12.12",
  organization := "com.softwaremill.sbt-softwaremill",
  sbtVersion in Global := {
    scalaBinaryVersion.value match {
      case "2.10" => "0.13.17"
      case "2.12" => "1.3.8"
    }
  }
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    name := "sbt-softwaremill-root",
    description := "Build configuration for SBT projects"
  )
  .settings(Publish.noPublishSettings)
  .aggregate(common, publish, extra, browserTestJs)

lazy val common = project
  .in(file("common"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-softwaremill-common",
    description := "Build configuration for SBT projects: common",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value)
  )
  .settings(
    addSbtPlugin(
      "org.scalameta" % "sbt-scalafmt" % BuildInfo.sbtScalafmtVersion
    )
  )

lazy val publish = project
  .in(file("publish"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-softwaremill-publish",
    description := "Build configuration for SBT projects: publishing",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
  )
  .settings(
    addSbtPlugin("com.geirsson" % "sbt-ci-release" % BuildInfo.sbtCiReleaseVersion)
  )

lazy val extra = project
  .in(file("extra"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-softwaremill-extra",
    description := "Build configuration for SBT projects: extra",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
  )
  .settings(
    addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13"),
    addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1"),
    addSbtPlugin("com.dwijnand" % "sbt-reloadquick" % "1.0.0"),
    addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1"),
    addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "3.0.0")
  )

lazy val browserTestJs = project
  .in(file("browser-test-js"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-softwaremill-browser-test-js",
    description := "Build configuration for SBT projects: browser test JS",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value)
  )
  .settings(
    libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.0",
    addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.3.1"),
  )