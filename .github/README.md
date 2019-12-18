<p align="center"><img src="https://i.imgur.com/Vsj2cT2.png"/></p>

* * *

**_Table of Contents_**

1.  [Dependencies](https://github.com/T145/metalchests#dependencies)
2.  [Workspace Setup](https://github.com/T145/metalchests#workspace-setup)
3.  [Project License](https://github.com/T145/metalchests#license)
4.  [Contributing](https://github.com/T145/metalchests#contributing)
5.  [Support](https://github.com/T145/metalchests#support)

* * *

## Workspace Dependencies

> _([First-Time Git Setup](https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup))_

### Windows

#### Using [Scoop](https://github.com/lukesampson/scoop/blob/master/README.md)

```bash
scoop bucket add java
scoop install git ojdkbuild10-full
```

### OSX

#### Using [Homebrew](https://brew.sh/)

```bash
# Git should be automatically installed alongside
# Homebrew through Apple's Command Line utilities
brew cask install java
```

* * *

## Workspace Setup

### Eclipse

```bash
gradlew genEclipseRuns
gradlew eclipse
```

Next, you'll need to install the [EditorConfig plugin](https://github.com/ncjones/editorconfig-eclipse#readme).
Navigate to `Help > Eclipse Marketplace`, and search for `editorconfig`.
There should only be one result: install it and you're all set.

### IntelliJ IDEA

```bash
gradlew genIntellijRuns
gradlew idea
```

> Be sure IDEA recognizes the `src/api/java` directory!

* * *

## Development

> Be sure you have `t145.metalchests.api` under your project's `src/api/java` directory!

To add upgrade compatibility to your own vanilla-like chests, you can just make this call:

```java
UpgradeRegistry.registerChest((supporting upgrade item), (your chest block), (target metal chest block));
```

> The upgrade target MUST use the `IMetalChest.VARIANT` property!

An example:

```java
UpgradeRegistry.register(ItemsMC.CHEST_UPGRADE, Blocks.CHEST, BlocksMC.METAL_CHEST);
```

* * *

## License

Mod source code is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
The actual workspace license is located in this project.
To use any mod assets, you may contatct [myself](https://github.com/T145) or the original creator for permission.

* * *

## Cryptocurrency Support

| Currency | Address |
|---|---|
| **Bitcoin** | `1FVPmQvZq7srRXT2KTBUva8G8Qy3ZotpDt` |
| **Bitcoin Cash** | `qrv6g7usyypntgaxt4fmyen53r7630r8ls40yn5pvx` |
| **Bitcoin Gold** | `Gedh2Xu6kRUDWzXu52uaZZD2X41YZg5jhJ` |
| **Bitcoin SV** | `qrd7vqxjzx4g3q7xfc9arfne3a6z5p0fqy0l3qkf9l` |
| **ARK** | `ALTate9wtGzuyw5Ko2oE185qwqRAL1ywMC` |
| **Binance Coin** | `bnb1nzl3rfk0m48gty5hyhwq474urscsvqgzzarlry` |
| **BitTorrent** | `TM9XyQDDJfoVpJ2WdWmeuFki91PkVGo9tH` |
| **Dash** | `XngmA2Enqi7cdMEEWcgeLEpoX77GHQEAxg` |
| **Decred** | `DsUEMtiQar1YbbZmykiikoKDtZbKAN4tpAg` |
| **DigiByte** | `DKgbbwb7sq3pEwdhCyVtnsc2aK1VFT53Xz` |
| **LISK** | `7664237101393086268L` |
| **Litecoin** | `LfDf59fTRzRGEzJbB77bDGfXLT51oQZsoo` |
| **Monero** | `4ASjK8q2E27UnkqLw9VjrmiAAB9qWFf9Hft9EbQKjkvpSWBmPNWbnfNDsk5WKzKqUDbrgjeG1HbmU1UHK6WBwcnzUo4R7kn` |
| **NEM** | `NCHWOE3BTXMXSLEVJYY752OBA3ZMWYJLE6MU5XCL` |
| **Neo** | `ANV167RSy7NUy5MSpsr6cUoQxU25qjyj29` |
| **Qtum** | `Qb3U7uXdgxwHyrpxRN4ZzAeG5i8nAyV9kg` |
| **Stellar** | `GCCCV6TLTGMHLVTZNTSK7FZKI5JF2YW5FUCHHFU73A7WF6F6TRW7B53E` |
| **Waves** | `3P9dSY8o8VxbTa69urh4AyVipSJcV7Cj31u` |
| **XRP / Ripple** | `rMGEUf8XnZnURRsbNWC9qVoGgTAgp5QWvF` |
| **Zcash** | `t1VEJe7e3Qzs367PnKj61AsSn6pyvedgckx` |
| **Ethereum** | `0xbE37272cE191C4B6f4376bD4248bB0Be48346964` |
| **ETH Classic** | `0x6dE6C773d450E05B38e8bD4D56D6f095D0680350` |
