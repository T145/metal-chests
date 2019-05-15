<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

***

[![DownloadCount](http://cf.way2muchnoise.eu/full_metalchests_downloads.svg)](https://minecraft.curseforge.com/projects/metalchests)

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
UpgradeRegistry.registerChest((upgrade item resource name), (default chest tile entity class), (block of desired upgrade));
```
> The block to upgrade to MUST use the `IMetalChest.VARIANT` property!

---

## License

Mod source code is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
The actual workspace license is located in this project.
To use any mod assets, you may contatct [myself](https://github.com/T145) or the original creator for permission.

---

## Support

<div align="center">

**Patreon**: [patreon.com/user=152139](https://www.patreon.com/user?u=152139)
</div>

<div align="center">

**Paypal**: [paypal.me/T145](https://www.paypal.me/T145)
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
