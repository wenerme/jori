# jori
Java algorithms learning


## Build
* [#7343](https://github.com/bazelbuild/bazel/issues/7343) - Bazel do not support lombok

```bash
# need delombok befor build
# after 0.24 need incompatible_disable_deprecated_attr_params
bazel build //:joricore-j2cl --incompatible_disable_deprecated_attr_params=false
```

## Why
[kori](https://github.com/wenerme/kori) is write in kotlin, because kotlin can compile to js, but with [j2cl](https://github.com/google/j2cl) Java can compile to js too, and Java is "easier" to write and understand. 

## j2cl pitfall
* java limitation
    * Regex
    * String.format
* library limitation
    * jsr350
        * Regex
    * guava
        * don't know how to transpile
        * guava support gwt suppose to be transpile-able
