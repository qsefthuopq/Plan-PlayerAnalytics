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
package com.djrapitops.plan.db.access.transactions.commands;

import com.djrapitops.plan.db.access.ExecStatement;
import com.djrapitops.plan.db.access.transactions.Transaction;
import com.djrapitops.plan.db.sql.tables.SecurityTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Transaction to remove a Plan {@link com.djrapitops.plan.data.WebUser} from the database.
 *
 * @author Rsl1122
 */
public class RemoveWebUserTransaction extends Transaction {

    private final String username;

    public RemoveWebUserTransaction(String username) {
        this.username = username;
    }

    @Override
    protected void performOperations() {
        String sql = "DELETE FROM " + SecurityTable.TABLE_NAME + " WHERE " + SecurityTable.USERNAME + "=?";

        execute(new ExecStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, username);
            }
        });
    }
}