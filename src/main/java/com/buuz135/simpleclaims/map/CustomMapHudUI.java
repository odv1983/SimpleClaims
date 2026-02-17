package com.buuz135.simpleclaims.map;

import com.google.gson.JsonObject;
import com.hypixel.hytale.builtin.creativehub.CreativeHubPlugin;
import com.hypixel.hytale.builtin.creativehub.config.CreativeHubEntityConfig;
import com.hypixel.hytale.builtin.creativehub.config.CreativeHubWorldConfig;
import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBinding;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.UpdateAnchorUI;
import com.hypixel.hytale.server.core.modules.anchoraction.AnchorActionModule;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.ISpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomMapHudUI {

    public static final String ANCHOR_ID = "MapServerContent";
    public static final String ACTION_RETURN_TO_HUB = "SimpleClaims-Interaction";

    private CustomMapHudUI() {
    }

    public static void register() {
        AnchorActionModule.get().register(ACTION_RETURN_TO_HUB, (playerRef, ref, store, data) -> executeInteraction(playerRef, ref, store, data));
    }

    public static void send(@Nonnull PlayerRef playerRef) {
        send(playerRef, false);
    }

    public static void send(@Nonnull PlayerRef playerRef, boolean disabled) {
        UICommandBuilder commandBuilder = new UICommandBuilder();
        UIEventBuilder eventBuilder = new UIEventBuilder();

        commandBuilder.append("Hud/Buuz135_SimpleClaims_MapButtons2.ui");
        /*commandBuilder.set("#ReturnToHubButton.Disabled", disabled);

        if (!disabled) {

        }*/
        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#EditPartyButton", EventData.of("action", ACTION_RETURN_TO_HUB).append("Extra", "AAAAA"), false);

        playerRef.getPacketHandler().writeNoCache(new UpdateAnchorUI(ANCHOR_ID, true, commandBuilder.getCommands(), eventBuilder.getEvents()));
    }

    public static void clear(@Nonnull PlayerRef playerRef) {
        playerRef.getPacketHandler().writeNoCache(new UpdateAnchorUI(ANCHOR_ID, true, (CustomUICommand[]) null, (CustomUIEventBinding[]) null));
    }

    public static void executeInteraction(@Nonnull PlayerRef playerRef, @Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, JsonObject data) {
        System.out.println("Interaction");
        System.out.println(data);
    }


}
