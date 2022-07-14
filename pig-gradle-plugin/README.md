# PIG Gradle Plugin

```groovy
plugins {
    id 'pig-gradle-plugin'
}
```

## Source Sets
```groovy
sourceSets {
    main {
        pig {
            // in addition to the default 'src/main/pig'
            srcDir 'path/to/type/universes'
        }
    }
    test {
        pig {
            // in addition to the default 'src/test/pig'
            srcDir 'path/to/test/type/universes'
        }
    }
}
```

## Tasks

```
pig {
  target = "kotlin" // required
  namespace = ... // optional
  template = ... // optional
  outDir = 'build/generated-sources/pig/' // default
}
```

## Dependencies

| Task Name                 | Depends On                    |
|---------------------------|-------------------------------|
| compileJava               | generatePigSource             |
| compileTestJava           | generatePigTestSource         |
| compile*SourceSet*Java    | generatePig*SourceSet*Source  |
