package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.ASpell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public interface ISpellCaster {
    // TODO: Save/load this map on log out/in
    @NotNull HashMap<ASpell, Integer> getCastSpellMap();

    void setOwner(@Nullable UUID value);
    @Nullable UUID getOwner();

    void setCasting(boolean value);
    boolean isCasting();

    void setCastingCurrent(float value);
    float getCastingCurrent();
}
