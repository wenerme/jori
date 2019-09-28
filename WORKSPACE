workspace(name = "me_wener_jori")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Java & Maven
RULES_JVM_EXTERNAL_TAG = "2.8"
RULES_JVM_EXTERNAL_SHA = "79c9850690d7614ecdb72d68394f994fef7534b292c4867ce5e7dec0aa7bdfad"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install", "artifact")

maven_install(
    artifacts = [
        "org.projectlombok:lombok:1.18.4",
    ],
    repositories = [
#        "https://maven-central.storage.googleapis.com",
        "http://central.maven.org/maven2",
#        "https://maven.aliyun.com/repository/central"
    ],
#    fetch_sources = True,
)
maven_install(
    artifacts = [
        "com.google.code.findbugs:jsr305:3.0.2",
        "com.google.guava:guava:28.1-jre",
        "com.google.jsinterop:jsinterop-annotations:2.0.0",
    ],
    repositories = [
#        "https://maven-central.storage.googleapis.com",
        "http://central.maven.org/maven2",
#        "https://maven.aliyun.com/repository/central"
    ],
    fetch_sources = True,
)


#http_archive(
#    name = "io_bazel_rules_closure",
##    sha256 = "b29a8bc2cb10513c864cb1084d6f38613ef14a143797cea0af0f91cd385f5e8c",
#    strip_prefix = "rules_closure-0.9.0",
#    urls = [
#        "https://mirror.bazel.build/github.com/bazelbuild/rules_closure/archive/0.9.0.tar.gz",
#        "https://github.com/bazelbuild/rules_closure/archive/0.9.0.tar.gz",
#    ],
#)
#load("@io_bazel_rules_closure//closure:defs.bzl", "closure_repositories")
#closure_repositories()

#load("@io_bazel_rules_closure//closure:defs.bzl", "closure_repositories")

#closure_repositories()

# Load j2cl repository
http_archive(
    name = "com_google_j2cl",
    strip_prefix = "j2cl-master",
    url = "https://github.com/google/j2cl/archive/master.zip",
)

load("@com_google_j2cl//build_defs:repository.bzl", "load_j2cl_repo_deps")
load_j2cl_repo_deps()

load("@com_google_j2cl//build_defs:rules.bzl", "setup_j2cl_workspace")
setup_j2cl_workspace()


