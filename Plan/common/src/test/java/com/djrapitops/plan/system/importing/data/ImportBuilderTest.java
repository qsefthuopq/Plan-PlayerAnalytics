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
package com.djrapitops.plan.system.importing.data;

import com.djrapitops.plan.data.container.PlayerKill;
import com.djrapitops.plan.data.container.TPS;
import com.djrapitops.plan.data.store.objects.Nickname;
import com.djrapitops.plan.data.time.GMTimes;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import utilities.RandomData;
import utilities.TestConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Tests for various {@link com.djrapitops.plan.system.importing.importers.Importer}s.
 *
 * @author Fuzzlemann
 */
public class ImportBuilderTest {

    private int randomInt = RandomData.randomInt(0, 10);
    private String randomString = RandomData.randomString(randomInt);

    @Test
    public void emptyServerBuilderInitializesCollections() {
        ServerImportData data = ServerImportData.builder().build();

        assertNotNull(data.getCommandUsages());
        assertNotNull(data.getTpsData());
    }

    @Test
    public void emptyUserBuilderInitializesSomeVariables() {
        UserImportData data = UserImportData.builder(TestConstants.SERVER_UUID).build();

        assertEquals(0, data.getRegistered());
        assertEquals(0, data.getTimesKicked());
        assertEquals(0, data.getMobKills());
        assertEquals(0, data.getDeaths());

        assertNull(data.getName());
        assertNull(data.getUuid());

        assertFalse(data.isOp());
        assertFalse(data.isBanned());

        assertNotNull(data.getWorldTimes());
        assertNotNull(data.getNicknames());
        assertNotNull(data.getKills());
        assertNotNull(data.getIps());

        assertTrue(data.getWorldTimes().isEmpty());
        assertTrue(data.getNicknames().isEmpty());
        assertTrue(data.getKills().isEmpty());
        assertTrue(data.getIps().isEmpty());
    }

    @Test
    public void serverDataBuilderConstructsCorrectItem() {
        ServerImportData.ServerImportDataBuilder builder = ServerImportData.builder();

        TPS tps = new TPS(randomInt, randomInt, randomInt, randomInt, randomInt, randomInt, randomInt, randomInt);

        ServerImportData data = builder.tpsData(tps)
                .tpsData(tps)
                .tpsData(tps)
                .tpsData(tps, tps, tps)
                .tpsData(Collections.emptyList())
                .tpsData(randomInt, randomInt, randomInt, randomInt, randomInt, randomInt, randomInt)
                .tpsData(Collections.singletonList(tps))
                .tpsData(Arrays.asList(tps, tps))
                .commandUsage(randomString, randomInt)
                .commandUsage(randomString, randomInt)
                .commandUsage(randomString, randomInt)
                .commandUsage(randomString, randomInt)
                .commandUsages(new HashMap<>())
                .commandUsages(ImmutableMap.of(randomString, randomInt))
                .build();

        assertEquals(10, data.getTpsData().size());
        assertEquals(1, data.getCommandUsages().size());

        assertEquals(randomInt, data.getTpsData().get(0).getDate());
    }

    @Test
    public void userDataBuilderConstructsCorrectItem() {
        UserImportData.UserImportDataBuilder builder = UserImportData.builder(TestConstants.SERVER_UUID);

        UUID uuid = UUID.randomUUID();
        PlayerKill playerKill = new PlayerKill(uuid, randomString, 1);
        GMTimes gmTimes = new GMTimes(randomString, randomInt);

        UserImportData data = builder.uuid(uuid)
                .banned()
                .banned(false)
                .op()
                .ips(randomString, randomString)
                .ips(Collections.singletonList(randomString))
                .kills(playerKill, playerKill, playerKill)
                .kills(Collections.singleton(playerKill))
                .name(randomString)
                .registered(randomInt)
                .timesKicked(randomInt)
                .mobKills(randomInt)
                .worldTimes(randomString, randomInt, randomInt, randomInt, randomInt)
                .worldTimes(randomString, gmTimes)
                .deaths(randomInt)
                .worldTimes(ImmutableMap.of(randomString, gmTimes))
                .nicknames(randomString, randomString)
                .nicknames(Collections.singletonList(new Nickname(randomString, System.currentTimeMillis(), TestConstants.SERVER_UUID)))
                .build();

        assertNotNull(data);

        assertFalse(data.isBanned());
        assertTrue(data.isOp());

        assertEquals(randomInt, data.getDeaths());
        assertEquals(1, data.getWorldTimes().size());
        assertEquals(3, data.getIps().size());
        assertEquals(playerKill, data.getKills().get(0));
        assertEquals(randomInt, data.getMobKills());
        assertEquals(3, data.getNicknames().size());
        assertEquals(randomInt, data.getTimesKicked());

        assertEquals(uuid, data.getUuid());
        assertEquals(randomString, data.getName());
    }
}
