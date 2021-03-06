/**
 * Copyright (c) 2016 YCSB contributors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
package com.yahoo.ycsb.db.flavors;

import com.yahoo.ycsb.db.JdbcDBClient;
import com.yahoo.ycsb.db.StatementType;

/**
 * A default flavor for relational databases.
 */
public class DefaultDBFlavor extends DBFlavor {
  public DefaultDBFlavor() {
    super(DBName.DEFAULT);
  }
  public DefaultDBFlavor(DBName dbName) {
    super(dbName);
  }

  @Override
  public String createInsertStatement(StatementType insertType, String key) {
    StringBuilder insert = new StringBuilder("INSERT INTO ");
    insert.append(insertType.getTableName());
    insert.append(" (" + JdbcDBClient.PRIMARY_KEY + ", ");
    insert.append(" FIELDS) VALUES (?, JSON_OBJECT( ");
    int count = 0;
    String[] fieldKeys = insertType.getFieldString().split(",");
    for(String field : fieldKeys) {
      insert.append("'" + field + "', ?");
      if(count < fieldKeys.length-1) {
        insert.append(", ");
      }
      count++;
    }
    insert.append("))");
    return insert.toString();
  }
// INSERT INTO usertable(YCSB_KEY, "user27849050938022") VALUES(?, ?, ?...)

  @Override
  public String createReadStatement(StatementType readType, String key) {
    StringBuilder read = new StringBuilder("SELECT * FROM ");
    read.append(readType.getTableName());
    read.append(" WHERE ");
    read.append(JdbcDBClient.PRIMARY_KEY);
    read.append(" = ");
    read.append("?");
    return read.toString();
  }
// SELECT * FROM usertable WHERE YCSB_KEY=?

  @Override
  public String createDeleteStatement(StatementType deleteType, String key) {
    StringBuilder delete = new StringBuilder("DELETE FROM ");
    delete.append(deleteType.getTableName());
    delete.append(" WHERE ");
    delete.append(JdbcDBClient.PRIMARY_KEY);
    delete.append(" = ?");
    return delete.toString();
  }
// DELETE FROM usertable WHERE YCSB_KEY=?

  @Override
  public String createUpdateStatement(StatementType updateType, String key) {
    String[] fieldKeys = updateType.getFieldString().split(",");
    StringBuilder update = new StringBuilder("UPDATE ");
    update.append(updateType.getTableName());
    update.append(" SET FIELDS = JSON_SET(FIELDS, ");
    for (int i = 0; i < fieldKeys.length; i++) {
      update.append("'$." + fieldKeys[i] + "', ? ");
      if(i < fieldKeys.length-1) {
        update.append(", ");
      }
    }
    update.append(") WHERE YCSB_KEY = ?");
    return update.toString();
  }
// UPDATE usertable SET FIELD2=?, FIELD4=?,... WHERE YCSB_KEY=?

  @Override
  public String createScanStatement(StatementType scanType, String key) {
    StringBuilder select = new StringBuilder("SELECT FIELDS FROM ");
    select.append(scanType.getTableName());
    select.append(" WHERE ");
    select.append(JdbcDBClient.PRIMARY_KEY);
    select.append(" >= ?");
    select.append(" ORDER BY ");
    select.append(JdbcDBClient.PRIMARY_KEY);
    select.append(" LIMIT ?");
    return select.toString();
  }
}
// SELECT * FROM usertable WHERE YCSB_KEY >= ? ORDER BY YCSB_KEY LIMIT ?
