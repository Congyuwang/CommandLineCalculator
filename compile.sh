#!/bin/bash

BASEDIR=$(dirname "$0")

cd "$BASEDIR" || exit

export JAVA_HOME=`/usr/libexec/java_home -v1.8`

javac -d out/class/ -sourcepath lib/big-math-2.3.0-sources.jar:lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:src/ src/*.java src/**/*.java

mkdir -p out/class/resource/

cp src/resource/HelpMenu.html out/class/resource/

cd "$BASEDIR/out/class/" || exit

jar cfe ../calculator.jar Calculator *

cp ../calculator.jar ~/Desktop/
