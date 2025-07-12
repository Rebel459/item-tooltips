# Item Tooltips

Public GitHub repository for Item Tooltips - licensed under LGPL-3.0 Only

### Tagging Items for Descriptions

Simply add any blocks / items you'd like to have descriptions for to the #item_tooltips:has_description tag. The translation keys simply add a ".desc" onto the end of the original item name, as with other existing enchantment description mods.

If you'd like to have additional description lines (to stop descriptions taking up too much horizontal space), you can add additional lines using the following tags (note that the item only needs to be tagged in the highest number tag).

```
#item_tooltips:has_description - ".desc"
#item_tooltips:has_description_second_line - ".desc.second"
#item_tooltips:has_description_third_line - ".desc.third"
#item_tooltips:has_description_fourth_line - ".desc.fourth"
#item_tooltips:has_description_fifth_line - ".desc.fifth"
```

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
tooltips_version=1.21.7-v1.0.0-fabric
```
