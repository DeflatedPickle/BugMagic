package com.deflatedpickle.bugmagic.api.capability;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ISpellCaster {
    void setOwner(@Nullable UUID value);
    @Nullable UUID getOwner();

    // HashMap<ResourceLocation, Integer> getActiveSpells();

    void setCasting(boolean value);
    boolean isCasting();

    void setCurrentCooldown(float value);
    float getCurrentCooldown();

    void setCastingLength(float value);
    float getCastingLength();
}
