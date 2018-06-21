<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

***

The better alternative to IronChests!

[![DownloadCount](http://cf.way2muchnoise.eu/metalchests.svg)](https://minecraft.curseforge.com/projects/metalchests)
[![SupportedVersions](http://cf.way2muchnoise.eu/versions/For%20MC%20_290145_all.svg)](https://minecraft.curseforge.com/projects/metalchests)

---
**_Table of Contents_**

1. [Dependencies](https://github.com/T145/metalchests#dependencies)
2. [Workspace Setup](https://github.com/T145/metalchests#workspace-setup)
3. [Project License](https://github.com/T145/metalchests#license)
4. [Dev Support](https://github.com/T145/metalchests#support)
5. [Contributing](https://github.com/T145/metalchests/blob/master/.github/CONTRIBUTING.md)

---

## Dependencies

In order to get started with Minecraft mod development in this workspace, a few prerequisites are required:

1. [Git](https://git-scm.com/downloads)
2. [Java Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (JDK 8)
3. *(Optional)* [Gradle](http://gradle.org/gradle-download/)

> If you don't manually install Gradle, just append a `w` to `gradle` command prefixes.
> This will install the Gradle wrapper once, and any proceeding commands should be executed using it.

### Windows
I highly recommend using [Chocolatey](https://chocolatey.org/install),
and installing everything by executing the following commands:
```bash
choco install jdk8
choco install gradle --version 4.4.1
```
I'd recommend running the Powershell variant.

### OSX
I highly recommend using [Homebrew](https://brew.sh/),
and installing everything by executing the following commands:
```bash
brew cask install java
brew install gradle --version 4.4.1
```
If you don't have Apple's Command Line Utilities installed before installing Homebrew, they will be installed automatically.
Be sure you're using an Administrator account if this is the case.

---

## Workspace Setup

If you plan to use the Eclipse IDE, then execute:
```bash
gradle setupEclipseWorkspace
```
else just run the typical:
```bash
gradle setupDecompWorkspace
```

Depending on your internet connection and the processing power of your machine, it may take a while to build.
For most people it takes about 10 minutes.

After that just run either `gradle eclipse` or `gradle idea`, depending on your IDE.

---

## License

Mod source code is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0). The actual workspace license is located in this project. To use any mod assets, you may contatct [myself](https://github.com/T145) or the original creator for permission.

---

## Support

<div align="center">

**Patreon**: [https://www.patreon.com/user?u=152139](https://www.patreon.com/user?u=152139)
</div>

<div align="center">

**Paypal**: [paypal.me/T145](paypal.me/T145)
</div>

<div align="center">

**Bitcoin**: `1qrrPQqfbfXLRqzS6jb7A7Mgnzz85Mjxu`
</div>

<div align="center">

**Ethereum**: `0x9dbafc51abe8ce05cac6f8a39ebd4351706840b0`
</div>

<div align="center">

**Litecoin**: `LiV9SfDjFYLFRCzf9wTf7ap8BuRF39J7PB`
</div>

<div align="center">

**Vertcoin**: `Vc6ss1NaitEtdrZZsDhQuv9pytKR5caiFy`
</div>
