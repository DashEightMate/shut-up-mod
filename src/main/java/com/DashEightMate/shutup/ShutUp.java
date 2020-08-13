package com.DashEightMate.shutup;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ShutUp implements ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "shut-up-mod";
    public static final String MOD_NAME = "Shut Up!";

    public static boolean isBlocking = false;
    public static boolean isOverridden = false;
    public static boolean isLoading = false;
    public static int numberBlocked = 0;
    private static boolean isInGame = false;
    private static long tickTimeStarted;

    public static List<String> avoidMessages = new ArrayList<>();

    public static KeyBinding overrideBinding;

    @Override
    public void onInitializeClient() {
        overrideBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "shutupmod.menu.overridebinding",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F1,
                "shutupmod.name"
        ));
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (isInGame && client.world.getTime() != 0 && tickTimeStarted == 0)
                tickTimeStarted = client.world.getTime();
            if (isInGame && client.world.getTime() == tickTimeStarted + 100) {
                isBlocking = false;
                /*client.player.sendMessage(Text.of("ShutUp blocked " + numberBlocked + " messages on startup. Have a great day!"),
                        false);*/
            }
        });
        log(Level.INFO, "Shut Up!                                   ...initialized");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static void gameJoined(MinecraftClient client){
        isLoading = false;
        isInGame = true;
        isBlocking = !isOverridden;
        /*for (int i = 0; i < 5; i++){
            client.player.sendMessage(Text.of("Test spam message"), false);
        }*/
    }

    public static void gameLeft(){
        isInGame = false;
        isBlocking = false;
        isOverridden = false;
    }

}