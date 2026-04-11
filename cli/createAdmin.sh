root=$(git rev-parse --show-toplevel 2>/dev/null) || {
  echo "Not inside a Git repository" >&2
  exit 1
}

"$root/gradlew" build
version=$("$root/gradlew" -q properties | grep "^version:" | awk '{print $2}')
jar="$root/build/libs/aedn-$version.jar"
java -jar "$jar" createAdmin
