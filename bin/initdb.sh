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
# Initdb Script for the SQLite Server
# -----------------------------------------------------------------------------

export JAVA_OPTS=-Xmx64m

if [ "$SQLITED_HOME" = "" ] ; then
    BIN_DIR=`dirname "$PRG"`
    export SQLITED_HOME=`dirname "$BIN_DIR"`
fi
if [ -z "$SQLITED_HOME" ] ; then
  echo "Error: SQLITED_HOME is not defined."
  exit 1
fi

exec "$SQLITED_HOME"/bin/sqlited.sh initdb "$@"
