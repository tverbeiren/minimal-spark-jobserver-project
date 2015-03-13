# minimal-spark-jobserver-project

This is the bare minimum required for creating a `jar` file that can be used with 
the [spark-jobserver](https://github.com/spark-jobserver/spark-jobserver).


In order to compile this, run the following from the `shell`:

```
sbt compile
sbt package
```

The resulting `jar` file under `target/scala-...` is the one you need to upload to the REST interface.
