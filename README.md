# Item Tooltips

Public GitHub repository for Item Tooltips - licensed under LGPL-3.0 Only

Available on [Modrinth](https://modrinth.com/mod/item-tooltips) & [CurseForge](https://www.curseforge.com/minecraft/mc-mods/item-tooltips)

### Depending in a Mod

You may depend on / integrate with Item Tooltips within your own projects. Make sure to change the version number and modloader to the correct ones in your gradle.properties

`build.gradle`

```
repositories {
	maven { url = "https://api.modrinth.com/maven" }
}
```

```
dependencies {
	modImplementation("maven.modrinth:item-tooltips:$tooltips_version")
}
```

`gradle.properties`

```
tooltips_version=1.21.8-v1.2.1-fabric
```
