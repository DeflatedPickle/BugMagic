package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.ASpell;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISpellLearner {
    @NotNull List<ASpell> getSpellList();

    void setCurrentIndex(int value);
    int getCurrentIndex();

    ASpell learnSpell(@NotNull ASpell spell);
}
