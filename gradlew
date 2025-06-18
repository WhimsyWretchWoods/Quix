#!/usr/bin/env sh

#
# Copyright 2011 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses "$JAVA_HOME/jre/sh/java" as the system JVM.
        JAVA_CMD="$JAVA_HOME/jre/sh/java"
    else
        JAVA_CMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVA_CMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME environment variable in your environment to match the
location of your Java installation."
    fi
else
    JAVA_CMD="java"
fi

APP_HOME=`dirname "$0"`
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

# Use the maximum available file descriptors.
[ -z "$FILE_DESCRIPTOR_LIMIT" ] && FILE_DESCRIPTOR_LIMIT="1024"
if [ "$FILE_DESCRIPTOR_LIMIT" -ne '-1' ]; then
  # Setting the limit is not allowed for non-root users on some systems.
  if [ $(id -u) -ne 0 ]; then
    MAX_FILE_DESCRIPTORS=`ulimit -n`
    if [ $? -eq 0 ]; then
      if [ "$MAX_FILE_DESCRIPTORS" -ne '-1' -a "$MAX_FILE_DESCRIPTORS" -lt "$FILE_DESCRIPTOR_LIMIT" ]; then
        FILE_DESCRIPTOR_LIMIT="$MAX_FILE_DESCRIPTORS"
      fi
    fi
  fi
  ulimit -n $FILE_DESCRIPTOR_LIMIT
fi

# OS specific support (must be 'true' or 'false').
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN*)
    cygwin=true
    ;;
  Darwin*)
    darwin=true
    ;;
  MINGW*)
    mingw=true
    ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything else.
if $cygwin ; then
    [ -n "$APP_HOME" ] && APP_HOME=`cygpath --unix "$APP_HOME"`
fi

# Set GRADLE_HOME to the directory of the Gradle distribution that this
# wrapper script belongs to.
GRADLE_HOME=`cd "$APP_HOME" && pwd`

# Set the location of the Gradle distribution to be used by the wrapper.
# If not set, the wrapper will download the distribution specified in
# gradle-wrapper.properties.
# GRADLE_DISTRIBUTION_URL=https://services.gradle.org/distributions/gradle-7.6-bin.zip

# Exit immediately if a command exits with a non-zero status.
set -e

# Load properties from gradle-wrapper.properties
GRADLE_WRAPPER_PROPERTIES_FILE="$GRADLE_HOME/gradle/wrapper/gradle-wrapper.properties"
if [ -f "$GRADLE_WRAPPER_PROPERTIES_FILE" ]; then
    # Use awk to parse properties, handling potential spaces in values
    WRAPPER_DISTRIBUTION_URL=$(awk -F'=' '/^distributionUrl=/ {print $2}' "$GRADLE_WRAPPER_PROPERTIES_FILE" | tr -d '\r')
    if [ -n "$WRAPPER_DISTRIBUTION_URL" ]; then
        GRADLE_DISTRIBUTION_URL="$WRAPPER_DISTRIBUTION_URL"
    fi
fi

# Determine if Gradle is already installed in the cache
GRADLE_USER_HOME_OPT=""
if [ -n "$GRADLE_USER_HOME" ]; then
    GRADLE_USER_HOME_OPT="-Dgradle.user.home=\"$GRADLE_USER_HOME\""
fi

if [ -z "$GRADLE_DISTRIBUTION_URL" ]; then
    GRADLE_DISTRIBUTION_URL_PARAM=""
else
    GRADLE_DISTRIBUTION_URL_PARAM="-Dgradle.wrapper.distribution=$GRADLE_DISTRIBUTION_URL"
fi

# Get the path to the wrapper JAR.
GRADLE_JAR="$GRADLE_HOME/gradle/wrapper/gradle-wrapper.jar"

# Check if wrapper JAR exists.
if [ ! -f "$GRADLE_JAR" ]; then
    echo "ERROR: Could not find gradle-wrapper.jar in $GRADLE_JAR."
    echo "Please ensure the wrapper JAR is present or use the 'gradle wrapper' task to regenerate."
    exit 1
fi

# Set the JVM arguments (includes GRADLE_OPTS and JAVA_OPTS).
JVM_OPTS="$DEFAULT_JVM_OPTS $GRADLE_OPTS $JAVA_OPTS"

exec "$JAVA_CMD" $JVM_OPTS $GRADLE_USER_HOME_OPT $GRADLE_DISTRIBUTION_URL_PARAM -classpath "$GRADLE_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
