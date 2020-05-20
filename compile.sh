#!/bin/bash

BASEDIR=$(dirname "$0")

cd "$BASEDIR" || exit

export JAVA_HOME=`/usr/libexec/java_home -v1.8`

javac -d out/class/ -sourcepath src/ src/*.java src/**/*.java

mkdir out/resource/

cp resource/HelpMenu.html out/resource/

cd "$BASEDIR/out/class/" || exit

jar cfe ../calculator.jar Calculator *
