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
package com.djrapitops.plan;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Thread that is run when JVM shuts down.
 * <p>
 * Saves active sessions to the Database (PlayerQuitEvent is not called)
 *
 * @author Rsl1122
 */
@Singleton
public class ShutdownHook extends Thread {

    private static ShutdownHook activated;

    private final ServerShutdownSave serverShutdownSave;

    @Inject
    public ShutdownHook(ServerShutdownSave serverShutdownSave) {
        this.serverShutdownSave = serverShutdownSave;
    }

    private static boolean isActivated() {
        return activated != null;
    }

    private static void activate(ShutdownHook hook) {
        activated = hook;
        Runtime.getRuntime().addShutdownHook(hook);
    }

    private static void deactivate() {
        Runtime.getRuntime().removeShutdownHook(activated);
        activated = null;
    }

    public void register() {
        if (isActivated()) {
            deactivate();
        }
        activate(this);
    }

    @Override
    public void run() {
        serverShutdownSave.serverIsKnownToBeShuttingDown();
        serverShutdownSave.performSave();
    }
}
