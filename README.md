# minimal-spark-jobserver-project

This is an example project for creating a `jar` file that can be used with 
the [spark-jobserver](https://github.com/spark-jobserver/spark-jobserver).

This is not the bare minimum anymore, but it may be the bare minimum for a useful config. Two endpoints are included:

- `Job1` initializes an `RDD` and a `Map`.
- `Job2` fetches both the content of the cached `RDD` and the `Map` and returns them in `JSON` format.

The `RDD` contains the contents of `input.string` as a distributed collection. The `Map` is the result of a wordcount peformed during the initialization (`Job1`).

## Running

In order to run this, something like the following should work:

```sh
curl -X DELETE '<server>:8090/contexts/minimal'
curl --data-binary @target/scala-2.10/minimal-spark-jobserver-project-assembly-0.1-SNAPSHOT.jar '<server>:8090/jars/minimal'
curl -d "" '<server>:8090/contexts/minimal'
curl -d "input.string = a b c a b see" '<server>:8090/jobs?appName=minimal&classPath=minimal.Job1&context=minimal&sync=true'
curl -d ""                             '<server>:8090/jobs?appName=minimal&classPath=minimal.Job2&context=minimal&sync=true'
```

If everything is right, something like this should be the result:

```
OKOKOK{
  "result": "Set(map:wc, rdd:words)"
}{
  "result": {
    "dd": ["a", "b", "c", "a", "b", "see"],
    "wc": {
      "b": 2,
      "a": 2,
      "see": 1,
      "c": 1
    }
  }
}%
```



## Compiling

In order to compile this, run the following from the `shell`:

```
sbt compile
sbt assembly
```

We use the `assembly` plugin for `sbt`. The resulting `jar` file under `target/scala-...` is the one you need to upload to the REST interface. See above for an example.

The `build.sbt` is tailored for the master branch of Spark-Jobserver where we used `sbt publish-local` to publish the binary distribution to our local ivy repo. This is what the `sbt` picks up.


## Remarks

Please note that you need the upcoming version 0.6.2 of Spark-Jobserver, or the master branch.

We have run into the issue described [here](https://github.com/spark-jobserver/spark-jobserver/issues/386). The workaround mentioned is applied in this version of the project. See `Job2.scala` for more info.
