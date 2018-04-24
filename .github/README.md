<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

***

The better alternative to IronChests!

[![DownloadCount](http://cf.way2muchnoise.eu/elemental-creepers-redux.svg)](https://minecraft.curseforge.com/projects/metalchests)
[![SupportedVersions](http://cf.way2muchnoise.eu/versions/For%20MC%20_metalchests_all.svg)](https://minecraft.curseforge.com/projects/metalchests)

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

Each of the listed requirements contains a hyperlink that should take you directly to the correspondant download page.
Just download and install what is compatible with your OS.
Gradle is optional is because this workspace includes a Gradle wrapper,
so when executing commands that begin with `gradle`,
execute them with `gradlew` instead and everything will function normally.

If you're using OSX, I highly recommend using [Homebrew](https://brew.sh/),
and installing everything by executing the following commands:
```bash
brew cask install java
brew install gradle
```
If you don't have Apple's Command Line Utilities installed before installing Homebrew, Hombrew will install them automatically.

---

## Workspace Setup

Execute a file in the `scripts` with the project directory as your working directory to build automatically:
```bash
./scripts/build*
```
If you don't have Gradle installed, just run the script that works with your OS and ends with a `w`;
this will install the Gradle wrapper once.
Depending on your internet connection and the processing power of your machine, it may take a while to build.
For most people it takes about 10 minutes.
Once it completes, run the script that corresponds to your IDE of choice in a similar manner to how the project was built.
```bash
./scripts/{eclipse* / idea*}
```
If you're using Eclipse, be sure to rename the project in the IDE to `elementalcreepers`.
This will force any installed Git plugins to reload.

---

## License

Please consult the [official license](http://www.apache.org/licenses/LICENSE-2.0) if you wish to use the source code.
To use any of the assets, you may contatct [myself](https://github.com/T145) or the original artist for permission.

---

## Support

If you like my work and are interested in supporting me, please go check out my [Patreon](https://www.patreon.com/user?u=152139)!
