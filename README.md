# Item Tooltips

Public GitHub repository for Item Tooltips - licensed under LGPL-3.0 Only

### Tagging Items for Descriptions

Simply add any blocks / items you'd like to add descriptions to to the #item_tooltips:has_description tag. The translation keys simply add a ".desc" onto the end of the original item name, as with other existing enchantment description mods.

### Depending in a mod

You may depend on / integrate with Item Tooltips within your own projects. Make sure to change the version number and modloader in your gradle.properties

`build.gradle`

```
dependencies {
	modImplementation("maven.modrinth:item-tooltips:$tooltips_version")
}
```

`gradle.properties`

```
tooltips_version=1.21.7-v1.0.0-fabric
```
