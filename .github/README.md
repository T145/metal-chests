<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

***

[![downloads](http://cf.way2muchnoise.eu/full_metalchests_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/metalchests)
[![versions](http://cf.way2muchnoise.eu/versions/metalchests.svg)](https://www.curseforge.com/minecraft/mc-mods/metalchests)
[![packs](http://cf.way2muchnoise.eu/packs/metalchests.svg)](https://www.curseforge.com/minecraft/mc-mods/metalchests/relations/dependents)

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

> Be sure you have `T145.metalchests.api` under your project's `src/api/java` directory!

To add upgrade compatibility to your own vanilla-like chests, you can just make this call:
```java
UpgradeRegistry.registerChest((supporting upgrade item), (your chest block), (target metal chest block));
```
> The upgrade target MUST use the `IMetalChest.VARIANT` property!

An example:
```java
UpgradeRegistry.register(ItemsMC.CHEST_UPGRADE, Blocks.CHEST, BlocksMC.METAL_CHEST);
```

---

## License

Mod source code is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
The actual workspace license is located in this project.
To use any mod assets, you may contatct [myself](https://github.com/T145) or the original creator for permission.

---

## Financial Support

# BTC

<div align="center">

**Bitcoin**: `1FVPmQvZq7srRXT2KTBUva8G8Qy3ZotpDt`
</div>

## Forks

<div align="center">

**Bitcoin Cash**: `qrv6g7usyypntgaxt4fmyen53r7630r8ls40yn5pvx`
</div>

<div align="center">

**Bitcoin Gold**: `Gedh2Xu6kRUDWzXu52uaZZD2X41YZg5jhJ`
</div>

<div align="center">

**Bitcoin SV**: `qrd7vqxjzx4g3q7xfc9arfne3a6z5p0fqy0l3qkf9l`
</div>

***

# Altcoins

<div align="center">

**ARK**: `ALTate9wtGzuyw5Ko2oE185qwqRAL1ywMC`
</div>

<div align="center">

**Binance Coin**: `bnb1nzl3rfk0m48gty5hyhwq474urscsvqgzzarlry`
</div>

<div align="center">

**BitTorrent**: `TM9XyQDDJfoVpJ2WdWmeuFki91PkVGo9tH`
</div>

<div align="center">

**Dash**: `XngmA2Enqi7cdMEEWcgeLEpoX77GHQEAxg`
</div>

<div align="center">

**Decred**: `DsUEMtiQar1YbbZmykiikoKDtZbKAN4tpAg`
</div>

<div align="center">

**DigiByte**: `DKgbbwb7sq3pEwdhCyVtnsc2aK1VFT53Xz`
</div>

<div align="center">

**LISK**: `7664237101393086268L`
</div>

<div align="center">

**Litecoin**: `LfDf59fTRzRGEzJbB77bDGfXLT51oQZsoo`
</div>

<div align="center">

**Monero**: `4ASjK8q2E27UnkqLw9VjrmiAAB9qWFf9Hft9EbQKjkvpSWBmPNWbnfNDsk5WKzKqUDbrgjeG1HbmU1UHK6WBwcnzUo4R7kn`
</div>

<div align="center">

**NEM**: `NCHWOE3BTXMXSLEVJYY752OBA3ZMWYJLE6MU5XCL`
</div>

<div align="center">

**Neo**: `ANV167RSy7NUy5MSpsr6cUoQxU25qjyj29`
</div>

<div align="center">

**Qtum**: `Qb3U7uXdgxwHyrpxRN4ZzAeG5i8nAyV9kg`
</div>

<div align="center">

**Stellar**: `GCCCV6TLTGMHLVTZNTSK7FZKI5JF2YW5FUCHHFU73A7WF6F6TRW7B53E`
</div>

<div align="center">

**Waves**: `3P9dSY8o8VxbTa69urh4AyVipSJcV7Cj31u`
</div>

<div align="center">

**XRP / Ripple**: `rMGEUf8XnZnURRsbNWC9qVoGgTAgp5QWvF`
</div>

<div align="center">

**Zcash**: `t1VEJe7e3Qzs367PnKj61AsSn6pyvedgckx`
</div>

***

# Ethereum Assets

<div align="center">

**Ethereum**: `0xbE37272cE191C4B6f4376bD4248bB0Be48346964`
</div>

<div align="center">

**Ethereum Classic**: `0x6dE6C773d450E05B38e8bD4D56D6f095D0680350`
</div>

<div align="center">

## Supported Tokens:
**0x**, *`Recommended`*

**AdToken**, *`NOT Recommended`*

**Aeron**,

**AirSwap**,

**Amber**, *`NOT Recommended`*

**AppCoins**,

**Aragon**,

**Augur**,

**Bancor**,

**Basic Attention Token**,

**BnkToTheFuture**, *`NOT Recommended`*

**Bread**,

**ChainLink**,

**Cindicator**,

**Civic**,

**Crypto.com**,

**Dai**, *`NOT Recommended`*

**Decentraland**,

**Dent**, *`NOT Recommended`*

**Dentacoin**,

**DigixDAO**,

**District0x**,

**Dragon**, *`NOT Recommended`*

**Edgeless**,

**Ethos**,

**FirstBlood**,

**FunFair**,

**Gemini Dollar**,

**GenesisVersion**,

**Gnosis**,

**Golem**, *`Recommended`*

**Iconomi**, *`NOT Recommended`*

**iExec RLC**,

**Kin**, *`NOT Recommended`*

**KuCoin**, *`NOT Recommended`*

**Kyber**,

**Loom**,

**Loopring**,

**Lunyr**,

**Maker**,

**Matchpool**,

**MediShares**, *`NOT Recommended`*

**Melonport**,

**Metal**,

**Mithril**,

**Numeraire**,

**OmiseGo**,

**Paxos**,

**Pillar**, *`Recommended`*

**Po.et**,

**Polymath**,

**Populous**,

**Power Ledger**,

**Qash**,

**Quantstamp**,

**Raiden**,

**Request**,

**Revain**,

**Ripio**,

**Rivetz**, *`NOT Recommended`*

**SALT**, *`Recommended`*

**Santiment**, *`NOT Recommended`*

**SingularDTV**,

**Status**,

**Storj**,

**Storm**,

**TAAS**, *`NOT Recommended`*

**TenX**,

**Tether USD**,

**Time Bank**, *`NOT Recommended`*

**Tron**, *`LOL`*

**TrueUSD**, *`Stable`*

**USD Coin**, *`Stable`*

**Veritaseum**, *`NOT Recommended`*

**Vibrate**,

**Waltonchain**, *`NOT Recommended`*

**Wax**,

**WeTrust**,

**Wings**
</div>
