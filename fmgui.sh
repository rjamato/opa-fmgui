#!/bin/sh
#
# $Id$
#
# Runs the Fabric Manager GUI.

INSTDIR=`dirname $0`

# Do our best to locate the appropriate version of Java
JAVA=java
if [ -x $INSTDIR/java/bin/java ]; then
    JAVA=$INSTDIR/java/bin/java
elif [ -x $JAVA_HOME/bin/java ]; then
    JAVA=$JAVA_HOME/bin/java
fi

# run the updater/launcher
exec $JAVA $* -jar /usr/share/java/fmgui/fmgui-10_0_0_0_190.jar
