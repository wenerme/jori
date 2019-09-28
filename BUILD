package(
    default_visibility = ["//visibility:public"],
    licenses = ["notice"],  # Apache 2.0
)
load("@rules_jvm_external//:defs.bzl", "maven_install", "artifact")
load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

java_library(
    name = "joricore-lib",
    srcs = glob(["jori-core/src/main/java/me/wener/jori/**/*.java"]),
    visibility=["//visibility:public"],
    deps = [
#      artifact("com.google.guava:guava"),
        "//third_party:guava",
      artifact("org.projectlombok:lombok"),
    ]
)

j2cl_library(
    name = "joricore-j2cl",
    srcs = glob([
        "jori-core/src/main/java/me/wener/jori/**/*.java",
        "jori-core/src/main/java/me/wener/jori/**/*.js",
    ]),
    deps = [
        "@com_google_j2cl//:jsinterop-annotations-j2cl",
        "//third_party:guava-j2cl",
#        "//third_party:jsr305_annotations-j2cl",
#              artifact("com.google.code.findbugs:jsr305"),

#        "//third_party:guava",
    ],
)
