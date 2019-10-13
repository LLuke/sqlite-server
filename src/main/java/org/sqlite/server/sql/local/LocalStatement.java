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
package org.sqlite.server.sql.local;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlite.server.SQLiteLocalDb;
import org.sqlite.server.SQLiteProcessor;
import org.sqlite.sql.SQLStatement;

/** A statement that executes in SQLite server local(session level) database.
 * 
 * @author little-pan
 * @since 2019-10-13
 *
 */
public abstract class LocalStatement extends SQLStatement {
    
    protected SQLiteLocalDb localDb;
    
    public LocalStatement(String sql) {
        super(sql);
    }
    
    public LocalStatement(String sql, String command) {
        super(sql, command);
    }
    
    @Override
    public SQLiteProcessor getContext() {
        return (SQLiteProcessor)super.getContext();
    }
    
    @Override
    public PreparedStatement prepare() throws SQLException, IllegalStateException {
        SQLiteProcessor context = getContext();
        this.localDb = context.attachLocalDb();
        this.localDb.register(this);
        
        return super.prepare();
    }
    
    @Override
    public void preExecute(int maxRows) throws SQLException, IllegalStateException {
        SQLiteProcessor context = getContext();
        this.localDb = context.attachLocalDb();
        this.localDb.register(this);
        
        super.preExecute(maxRows);
    }
    
    @Override
    public String getExecutableSQL() throws SQLException {
        String localSchema = this.localDb.getSchemaName();
        return getSQL(localSchema);
    }
    
    protected abstract String getSQL(String localSchema) throws SQLException;
    
    protected String getUpdatableSQL(String localSchema, boolean zeroRow) throws SQLException {
        String f = "update '%s'.updatable set value = value + 1 where id = %d";
        return format(f, localSchema, (zeroRow? 0: 1));
    }
    
    @Override
    public void close() {
        this.localDb.deregister(this);
        super.close();
    }
    
}
