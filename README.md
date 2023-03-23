# BackUP

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![jitpack](https://jitpack.io/v/hteppl/BackUP.svg)](https://jitpack.io/#hteppl/BackUP)

Simple backup plugin for Nukkit Minecraft Bedrock core (and forks), that will help you to create backups with ease.

Please note, that this plugin will work only on UNIX-based OS, because of
[**Parallel Virtual File System**](https://en.wikipedia.org/wiki/Parallel_Virtual_File_System) needed.

## Libraries

[**Nukkit**](https://github.com/CloudburstMC/Nukkit) is nuclear-powered server software for Minecraft: Pocket Edition
(you can also use PowerNukkit or PowerNukkitX).

## How to install

You just need to download and put `BackUP` plugin in `plugins` folder. Usually it will be
enough. Also, you can configure some default settings in `config.yml`.

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.hteppl</groupId>
    <artifactId>BackUP</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.hteppl:BackUP:1.0.0-SNAPSHOT'
}
```

## Configuration

Default plugin `config.yml` settings.

```yaml
# backups frequency time in days
frequency: 2
# compression level 0-9
compression: 9
# default backups folder (near plugins, worlds, etc.)
path: "backups"
# log compressed files names in console
log: false
```