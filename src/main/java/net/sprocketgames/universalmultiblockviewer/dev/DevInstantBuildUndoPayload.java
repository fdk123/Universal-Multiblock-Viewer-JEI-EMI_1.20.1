package net.sprocketgames.universalmultiblockviewer.dev;

import net.minecraft.network.FriendlyByteBuf;

/** Requests a safe undo of the sender's latest Build Here placement. */
public record DevInstantBuildUndoPayload() {
    public static void write(DevInstantBuildUndoPayload payload, FriendlyByteBuf buffer) { }
    public static DevInstantBuildUndoPayload read(FriendlyByteBuf buffer) {
        return new DevInstantBuildUndoPayload();
    }
}
