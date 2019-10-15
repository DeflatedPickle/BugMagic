package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.ASpell;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISpellLearner {
    @NotNull List<ASpell> getSpellList();

    // Note: Could be moved to the wand, as a spell ID, so you can set the wand to a spell and share it around
    void setCurrentIndex(int value);
    int getCurrentIndex();

    ASpell learnSpell(@NotNull ASpell spell);
}
