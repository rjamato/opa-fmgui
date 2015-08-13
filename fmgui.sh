#!/bin/sh
#
# $Id$
#
# Runs the Fabric Manager GUI.

INSTDIR=`dirname $0`

# Do our best to locate the appropriate version of Java
JAVA=java
JAR=/usr/share/java/fmgui/fmgui-10_0_0_0_2.jar
if [ -x $INSTDIR/java/bin/java ]; then
    JAVA=$INSTDIR/java/bin/java
elif [ -x $JAVA_HOME/bin/java ]; then
    JAVA=$JAVA_HOME/bin/java
fi
if [ -a $INSTDIR/fmgui-10_0_0_0_2.jar ]; then
    JAR=$INSTDIR/fmgui-10_0_0_0_2.jar
fi

# run the updater/launcher
exec $JAVA $* -jar $JAR
