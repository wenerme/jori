package(
    default_visibility = ["//:__subpackages__"],
    licenses = ["notice"],  # Apache 2.0
)
load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

alias(
    name = "guava",
    actual = "@com_google_guava",
)

alias(
    name = "auto_value",
    actual = "@com_google_auto_value",
)

alias(
    name = "auto_service",
    actual = "@com_google_auto_service//jar",
)

alias(
    name = "auto_common",
    actual = "@com_google_auto_common//jar",
)

alias(
    name = "gson",
    actual = "@com_google_code_gson",
)

alias(
    name = "jsr305_annotations",
    actual = "@com_google_code_findbugs_jsr305",
)

j2cl_library(
    name = "jsr305_annotations-j2cl",
    srcs = [
        "@com_google_code_findbugs_jsr305"
    ],
)

j2cl_library(
    name = "guava-j2cl",
    srcs = [
#        "@com_google_guava_guava",
"@maven//:v1/http/central.maven.org/maven2/com/google/guava/guava/28.1-jre/guava-28.1-jre-sources.jar"
#"@maven//:v1/http/central.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2-sources.jar",
    ],
)
