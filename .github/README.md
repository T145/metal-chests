<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

***

[![downloads](http://cf.way2muchnoise.eu/full_metalchests_downloads.svg)](https://minecraft.curseforge.com/projects/metalchests)
[![versions](http://cf.way2muchnoise.eu/versions/metalchests.svg)](https://minecraft.curseforge.com/projects/metalchests)
[![packs](http://cf.way2muchnoise.eu/packs/metalchests.svg)](https://minecraft.curseforge.com/projects/metalchests)

---
**_Table of Contents_**

1. [Dependencies](https://github.com/T145/metalchests#dependencies)
2. [Workspace Setup](https://github.com/T145/metalchests#workspace-setup)
3. [Project License](https://github.com/T145/metalchests#license)
4. [Dev Support](https://github.com/T145/metalchests#support)
5. [Contributing](https://github.com/T145/metalchests/blob/master/.github/CONTRIBUTING.md)

---

## Workspace Dependencies

> *([First-Time Git Setup](https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup))*

### Windows

#### Using [Scoop](https://github.com/lukesampson/scoop/blob/master/README.md) *(Recommended)*
```bash
scoop bucket add java
scoop bucket add versions
scoop install git ojdkbuild8 gradle4
```

#### Using [Chocolatey](https://chocolatey.org/install)
```bash
choco install git
choco install jdk8
choco install gradle
```

### OSX

#### Using [Homebrew](https://brew.sh/)
```bash
# Git should be automatically installed alongside
# Homebrew through Apple's Command Line utilities
brew cask install java
brew install gradle
```

---

## Workspace Setup

### Eclipse
```bash
gradle setupEclipseWorkspace
gradle eclipse
```

Next, you'll need to install the [EditorConfig plugin](https://github.com/ncjones/editorconfig-eclipse#readme).
Navigate to `Help > Eclipse Marketplace`, and search for `editorconfig`.
There should only be one result: install it and you're all set.

### IntelliJ IDEA

```bash
gradle setupDecompWorkspace
gradle idea
```
> Be sure IDEA recognizes the `src/api/java` directory!

---

## Development

To add upgrade compatibility to your own vanilla-like chests, you can just make this call:
```java
UpgradeRegistry.registerChest((your chest block), (target metal chest block));
```
> The upgrade target MUST use the `IMetalChest.VARIANT` property!

---

## License

Mod source code is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
The actual workspace license is located in this project.
To use any mod assets, you may contatct [myself](https://github.com/T145) or the original creator for permission.

---

## Financial Support

<div align="center">

**Patreon**: [patreon.com/user=152139](https://www.patreon.com/user?u=152139)
</div>

<div align="center">

**Paypal**: *Check the top right of the [CurseForge page](https://minecraft.curseforge.com/projects/metalchests)!*
</div>

<div align="center">

**Cryptocurrency**: [Check here!](https://github.com/T145/metalchests/blob/master/.github/CRYPTO_ADDRESSES.md)
</div>