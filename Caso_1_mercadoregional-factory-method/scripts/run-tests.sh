#!/usr/bin/env bash
# Compila y ejecuta las pruebas con JUnit Platform Console; opcionalmente mide cobertura con JaCoCo.
# Uso: scripts/run-tests.sh [--cobertura]
set -euo pipefail
cd "$(dirname "$0")/.."
J=/usr/share/java
OUT=target; rm -rf $OUT; mkdir -p $OUT/main $OUT/test
javac --release 17 -d $OUT/main $(find src/main/java -name '*.java')
CP_TEST="$J/junit-jupiter-api.jar:$J/opentest4j.jar:$J/junit-platform-commons.jar:$J/apiguardian-api.jar"
javac --release 17 -cp "$OUT/main:$CP_TEST" -d $OUT/test $(find src/test/java -name '*.java')
AGENT=""
if [ "${1:-}" = "--cobertura" ]; then
  AGENT="-javaagent:/opt/jacoco/jacocoagent.jar=destfile=$OUT/jacoco.exec,includes=mercadoregional.*"
fi
java $AGENT -jar $J/junit-platform-console-standalone.jar \
  --class-path "$OUT/main:$OUT/test" --scan-class-path "$OUT/test" --disable-banner --details=summary
if [ -n "$AGENT" ]; then
  javac -d $OUT/tools -cp "$J/org.jacoco.core.jar:$J/org.jacoco.report.jar:$J/asm.jar" tools/Cobertura.java
  java -cp "$OUT/tools:$J/org.jacoco.core.jar:$J/org.jacoco.report.jar:$J/asm.jar:$J/asm-commons.jar:$J/asm-tree.jar" \
    Cobertura $OUT/jacoco.exec $OUT/main
fi
