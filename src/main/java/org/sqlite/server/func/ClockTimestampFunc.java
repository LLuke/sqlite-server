/**
 * Copyright 2019 little-pan. A SQLite server based on the C/S architecture.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sqlite.server.func;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sqlite.Function;

/** Function clock_timestamp(), not thread-safe.
 * 
 * @author little-pan
 * @since 2019-09-18
 *
 */
public class ClockTimestampFunc extends Function {
    
    protected final DateFormat formater;
    
    public ClockTimestampFunc(String format) {
        this.formater = new SimpleDateFormat(format);
    }
    
    public ClockTimestampFunc() {
        this("yyyy-MM-dd HH:mm:ss.SSSZ");
    }

    @Override
    protected void xFunc() throws SQLException {
        super.result(this.formater.format(new Date()));
    }

}
