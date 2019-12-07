#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# -----------------------------------------------------------------------------
# Initdb/boot Script for the SQLite Server
# -----------------------------------------------------------------------------

PRGDIR=`dirname "$PRG"`
export SQLITED_HOME=`dirname "$PRGDIR"`
CLASSPATH=.:"$SQLITED_HOME"/lib/sqlite-jdbc-3.28.0.jar:"$SQLITED_HOME"/lib/logback-classic-1.1.7.jar:\
"$SQLITED_HOME"/lib/logback-core-1.1.7.jar:"$SQLITED_HOME"/lib/slf4j-api-1.7.21.jar:\
"$SQLITED_HOME"/lib/sqlite-server-0.3.29.jar:$SQLITED_HOME/conf

java -Xmx128m -classpath "$CLASSPATH" "$@" &