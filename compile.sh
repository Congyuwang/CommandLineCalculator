#!/bin/bash

BASEDIR=$(dirname "$0")

cd $BASEDIR || exit

javac -d out/class/ -sourcepath src/ src/*.java src/**/*.java

cd $BASEDIR/out/class/ || exit

jar --create --file ../calculator.jar --main-class CalculatorUI *
