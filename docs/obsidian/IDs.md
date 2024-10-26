#character #cubes

Every characteristic has an ID. It's a unique value which can be used to fetch it's value from character. IDs also used as internationalization keys.

As example `character.attribute.strength` is an ID for Strength Attribute.

Why? To make combinations easier. e.g. Running speed is `20 + 3 * DEX` so it could be described as `character.characteristic.running` is `character.attribute.dexterity * 3 + 20`

Some pseudo code
```kotlin
val runningSpeed = character.get("character.characteristic.running")
```

```kotlin
val characteristics = HashMap<String, () -> Integer>()
characteristics["character.characteristic.running"] = {
	attribute("character.attribute.dexterity") * 3 + 20
}
```
