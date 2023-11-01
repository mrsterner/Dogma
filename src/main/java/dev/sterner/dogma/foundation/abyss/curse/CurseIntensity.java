package dev.sterner.dogma.foundation.abyss.curse;


import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum CurseIntensity implements StringRepresentable {
    NONE(0, "none"),
    WEAK(1, "weak"),
    MEDIUM(2, "medium"),
    STRONG(3, "strong"),
    SEVERE(4, "severe");

    private static final IntFunction<CurseIntensity> BY_ID = ByIdMap.continuous(CurseIntensity::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final int id;
    private final String name;

    CurseIntensity(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this == NONE ? NONE.name : this == WEAK ? WEAK.name : this == MEDIUM ? MEDIUM.name : this == STRONG ? STRONG.name : SEVERE.name;
    }

    public int getId() {
        return this.id;
    }

    public static CurseIntensity byId(int id) {
        return BY_ID.apply(id);
    }
}