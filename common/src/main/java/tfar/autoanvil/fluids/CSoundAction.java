/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package tfar.autoanvil.fluids;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines an action which produces a sound.
 */
public record CSoundAction(String name) {
    private static final Map<String, CSoundAction> ACTIONS = new ConcurrentHashMap<>();

    /**
     * Gets or creates a new {@code SoundAction} for the given name.
     *
     * @param name the name of the action
     * @return the existing {@code SoundAction}, or a new one if not present
     */
    public static CSoundAction get(String name) {
        return ACTIONS.computeIfAbsent(name, CSoundAction::new);
    }
}