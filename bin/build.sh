#!/bin/sh
if [ -z "$JAVA_HOME" ] ; then
  if [[ "$OSTYPE" == "darwin"* ]]; then
    if [ -d "/System/Library/Frameworks/JavaVM.framework/Home" ] ; then
      export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Home
    else
      export JAVA_HOME=`/usr/libexec/java_home`
    fi
  fi
fi
if [ -z "$JAVA_HOME" ] ; then
  echo "Error: JAVA_HOME is not defined."
  exit 1
fi

if [ "$SQLITED_HOME" = "" ] ; then
  BIN_DIR=`dirname "$PRG"`
  export SQLITED_HOME=`dirname "$BIN_DIR"`
fi
if [ ! -d "$SQLITED_HOME" ] ; then
  echo "Error: SQLITED_HOME is not defined correctly."
  exit 1
fi

CLEAN_ARG=
TEST_ARG=
JAR_ARG=
for i in "$@"
do
  if [ "$i" = "jar" ] ; then export JAR_ARG="$i" ; fi
  if [ "$i" = "test" ] ; then export TEST_ARG="$i" ; fi
  if [ "$i" = "clean" ] ; then export CLEAN_ARG="$i" ; fi
done

if [ "$CLEAN_ARG" = "clean" ] ; then
  echo "clean: remove temp, test-lib, logs and target directories"
  rm -rf "$SQLITED_HOME/temp" "$SQLITED_HOME/test-lib" "$SQLITED_HOME/logs" "$SQLITED_HOME/target"
fi

if [ "$TEST_ARG" = "test" ] ; then
  echo "Test: run all test cases"
  rm -rf "$SQLITED_HOME/temp"
  if [ ! -d "$SQLITED_HOME/temp" ] ; then mkdir "$SQLITED_HOME/temp" ; fi
  if [ ! -d "$SQLITED_HOME/test-lib" ] ; then mkdir "$SQLITED_HOME/test-lib" ; fi
  if [ ! -d "$SQLITED_HOME/logs" ] ; then mkdir "$SQLITED_HOME/logs" ; fi
  if [ ! -d "$SQLITED_HOME/target" ] ; then mkdir "$SQLITED_HOME/target" ; fi
  
  mvn package -Dmaven.test.skip=true
  
  mvn dependency:copy-dependencies -DincludeScope=compile -DoutputDirectory="$SQLITED_HOME"/lib
  cp "$SQLITED_HOME"/target/*.jar "$SQLITED_HOME"/lib
  "$SQLITED_HOME"/bin/initdb.sh -D "$SQLITED_HOME"/temp -p 123456 -d test
  if [ $? != 0 ] ; then
    echo "Test initdb failed!"
    exit 1
  fi
  echo "Test initdb ok"
  
  mvn dependency:copy-dependencies -DoutputDirectory="$SQLITED_HOME"/test-lib
  CLASSPATH="$SQLITED_HOME"/target/classes:"$SQLITED_HOME"/target/test-classes
  for jar in "$SQLITED_HOME"/test-lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$jar
  done
  java -Xmx256m -classpath "$CLASSPATH" org.sqlite.TestAll
  if [ $? != 0 ] ; then
    echo "Test all test cases failed!"
    exit 1
  fi
  echo "Test all test cases ok"
fi

if [ "$JAR_ARG" != "" ] ; then
  echo "jar: package sqlite server"
  if [ -d "$SQLITED_HOME/lib" ] ; then rm -rf "$SQLITED_HOME"/lib ; fi
  if [ ! -d "$SQLITED_HOME/lib" ] ; then mkdir "$SQLITED_HOME"/lib ; fi
  mvn package -Dmaven.test.skip=true
  mvn dependency:copy-dependencies -DincludeScope=compile -DoutputDirectory="$SQLITED_HOME"/lib
  cp "$SQLITED_HOME"/target/*.jar "$SQLITED_HOME"/lib
  if [ $? != 0 ] ; then
    echo "Package sqlite server failed!"
    exit 1
  fi
  echo "Package sqlite server ok"
fi
