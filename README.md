# mal-java-client
A simple Java (JAX-RS/Jersey) client library to the MyAnimeList API.

This is a straight-forward JAX-RS client binding over the [documented][mal api documentation] and undocumented API for
the popular anime and manga tracking and discussion site [MyAnimeList.net][mal homepage].

## Contributing

*Contribute!!* Code, suggestions, critiques! Let's make this the #1 java library for interacting with MyAnimeList. We
are working towards [Release 0.1.0][milestones].

## Building

This library is a work in progress and has not been released yet so there is not a maven artifact yet.

The client library can be built and tested with [Gradle][gradle user guide] using either your local installation of
Gradle or the included wrapper (no installation required, use `./gradlew` or `./gradlew.bat`). To build
a jar which can be used in your project simply grab a copy of this repository and run `gradle build` and find the
produced `JAR` at `build/libs/mal-java-client-0.1.0-SNAPSHOT.jar`.

```
> gradle build

:compileJava
:compileGroovy UP-TO-DATE
:processResources UP-TO-DATE
:classes
:jar
:assemble
:compileTestJava
:compileTestGroovy UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses
:test
:check
:build

BUILD SUCCESSFUL

Total time: 2.809 secs
```

### Lombok

This project uses [Project Lombok][lombok features] to reduce boiler-plate code. If you plan on hacking on the code, you
may want to teach your IDE about Lombok. If you are using an Eclipse based IDE, you should be able to run `gradle
install` to configure the IDE with Lombok support. If you are using IntelliJ grab the
[Lombok Plugin][lombok intellij plugin].

<!--
 Link References
-->

[gradle user guide]:      https://docs.gradle.org/current/userguide/userguide.html
[lombok features]:        https://projectlombok.org/features/index.html
[lombok intellij plugin]: https://github.com/mplushnikov/lombok-intellij-plugin
[mal api documentation]:  http://myanimelist.net/modules.php?go=api
[mal homepage]:           http://myanimelist.net
[milestones]:             https://github.com/bendoerr/mal-java-client/milestones

<!--
vim: ft=markdown:tw=120
-->
