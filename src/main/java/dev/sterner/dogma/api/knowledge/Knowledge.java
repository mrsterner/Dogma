package dev.sterner.dogma.api.knowledge;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Knowledge {
    public final ResourceLocation icon;
    public final String identifier;
    public final List<Knowledge> children;

    public Knowledge(String identifier, ResourceLocation icon, List<Knowledge> children) {
        this.icon = icon;
        this.identifier = identifier;
        this.children = children;
    }
}