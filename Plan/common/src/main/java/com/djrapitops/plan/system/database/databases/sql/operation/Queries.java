/*
 *  This file is part of Player Analytics (Plan).
 *
 *  Plan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License v3 as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Plan. If not, see <https://www.gnu.org/licenses/>.
 */
package com.djrapitops.plan.system.database.databases.sql.operation;

import com.djrapitops.plan.system.database.databases.sql.objects.ForeignKeyConstraint;
import com.djrapitops.plan.system.database.databases.sql.processing.QueryStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains different SELECT statements.
 *
 * @author Rsl1122
 */
public class Queries {

    private Queries() {
        /* Static method class */
    }

    public static QueryStatement<List<ForeignKeyConstraint>> foreignKeyConstraintsOf(String tableSchema, String referencedTable) {
        String keySQL = "SELECT TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE" +
                " WHERE REFERENCED_TABLE_SCHEMA = ?" +
                " AND REFERENCED_TABLE_NAME = ?";
        return new QueryStatement<List<ForeignKeyConstraint>>(keySQL) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, tableSchema);
                statement.setString(2, referencedTable);
            }

            @Override
            public List<ForeignKeyConstraint> processResults(ResultSet set) throws SQLException {
                List<ForeignKeyConstraint> constraints = new ArrayList<>();

                while (set.next()) {
                    String table = set.getString("TABLE_NAME");
                    String referencedTable = set.getString("REFERENCED_TABLE_NAME");
                    String column = set.getString("COLUMN_NAME");
                    String referencedColumn = set.getString("REFERENCED_COLUMN_NAME");
                    String constraintName = set.getString("CONSTRAINT_NAME");

                    constraints.add(new ForeignKeyConstraint(
                            table, referencedTable,
                            column, referencedColumn,
                            constraintName
                    ));
                }

                return constraints;
            }
        };
    }

}