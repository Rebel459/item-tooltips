# Item Tooltips

Public GitHub repository for Item Tooltips - licensed under LGPL-3.0 Only

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
