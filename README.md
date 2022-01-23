# kMap
kMap is a generator. It generates mappers based on annotations found on the classes that need these mappings. 
Take for example these two classes:

```kotlin
@MapPartner(Dimension::class)
class Size {                                    class Dimension {
  @kMap var w: Int = 0                            var w: Int = 0
  @kMap var h: Int = 0                            var h: Int = 0
}                                               }
```

This will generate these files (they won't look exactly like this):
```kotlin
// SizeMapper.kt                                // DimensionMapper.kt
fun Size.toDimension() =                        fun Dimension.toSize() =
  Dimension().also {                              Size().also { 
    it.w = w                                        it.w = w
    it.h = h                                        it.h = h
  }                                               }
```
Which is very convenient to call:
```kotlin
Size(w = 15, h = 284).toDimension() // => Dimension(w = 15, h = 284)
```
