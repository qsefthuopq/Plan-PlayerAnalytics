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
package com.djrapitops.plan.db;

import com.djrapitops.plan.db.access.transactions.Transaction;
import com.djrapitops.plan.db.patches.Patch;
import com.djrapitops.plan.db.sql.tables.*;
import utilities.TestConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class DBPatchRegressionTest {

    String insertServer = "INSERT INTO plan_servers (uuid) VALUES ('" + TestConstants.SERVER_UUID + "')";
    String insertUser = "INSERT INTO plan_users (uuid, name, registered) VALUES ('" + TestConstants.PLAYER_ONE_UUID + "', 'TestName', 1234)";
    String insertUser2 = "INSERT INTO plan_users (uuid, name, registered) VALUES ('" + TestConstants.PLAYER_TWO_UUID + "', 'TestName2', 1234)";
    String insertUserInfo = "INSERT INTO plan_user_info (user_id, registered, server_id) VALUES (1, 1234, 1)";
    String insertIP = "INSERT INTO plan_ips (user_id, ip, geolocation, ip_hash, last_used) VALUES (1, '1.1.1.1', 'Finland', 'hash', 1234)";
    String insertNickname = "INSERT INTO plan_nicknames (user_id, nickname, server_id, last_used) VALUES (1, 'Nickname', 1, 1234)";
    String insertSession = "INSERT INTO plan_sessions (user_id, server_id, session_start, session_end, mob_kills, deaths, afk_time) VALUES (1,1,1234,5678,2,2,2)";
    String insertKill = "INSERT INTO plan_kills (killer_id, session_id, server_id, victim_id,  weapon, date) VALUES (1,1,1, 2, 'Sword', 3456)";
    String insertWorld = "INSERT INTO plan_worlds (server_id, world_name) VALUES (1, 'World')";
    String insertWorldTimes = "INSERT INTO plan_world_times (user_id, server_id, world_id, session_id, survival_time) VALUES (1,1,1,1,1234)";

    protected void dropAllTables(SQLDB underTest) {
        underTest.executeTransaction(new Transaction() {
            @Override
            protected void performOperations() {
                execute("DROP TABLE " + CommandUseTable.TABLE_NAME);
                execute("DROP TABLE " + GeoInfoTable.TABLE_NAME);
                execute("DROP TABLE " + KillsTable.TABLE_NAME);
                execute("DROP TABLE " + NicknamesTable.TABLE_NAME);
                execute("DROP TABLE " + PingTable.TABLE_NAME);
                execute("DROP TABLE " + SecurityTable.TABLE_NAME);
                execute("DROP TABLE " + ServerTable.TABLE_NAME);
                execute("DROP TABLE " + SessionsTable.TABLE_NAME);
                execute("DROP TABLE " + SettingsTable.TABLE_NAME);
                execute("DROP TABLE " + TPSTable.TABLE_NAME);
                execute("DROP TABLE " + UserInfoTable.TABLE_NAME);
                execute("DROP TABLE " + UsersTable.TABLE_NAME);
                execute("DROP TABLE " + WorldTable.TABLE_NAME);
                execute("DROP TABLE " + WorldTimesTable.TABLE_NAME);
            }
        });
    }

    protected void insertData(SQLDB underTest) {
        underTest.executeTransaction(new Transaction() {
            @Override
            protected void performOperations() {
                execute(insertServer);
                execute(insertUser);
                execute(insertUser2);
                execute(insertUserInfo);
                execute(insertIP);
                execute(insertNickname);
                execute(insertSession);
                execute(insertKill);
                execute(insertWorld);
                execute(insertWorldTimes);
            }
        });
    }

    protected void assertPatchesHaveBeenApplied(Patch[] patches) {
        List<String> failed = new ArrayList<>();
        for (Patch patch : patches) {
            if (!patch.hasBeenApplied()) {
                failed.add(patch.getClass().getSimpleName());
            }
        }
        assertTrue("Patches " + failed + " were not applied properly.", failed.isEmpty());
    }
}
