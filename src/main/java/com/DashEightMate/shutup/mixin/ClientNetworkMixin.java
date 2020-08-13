package com.DashEightMate.shutup.mixin;

import com.DashEightMate.shutup.ShutUp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientNetworkMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onGameJoin", at=@At("TAIL"))
    private void onClientGameStart(CallbackInfo ci){
        ShutUp.gameJoined(client);
    }

    @Inject(method="clearWorld", at=@At("HEAD"))
    private void onClientGameLeave(CallbackInfo ci){
        ShutUp.gameLeft();
    }
    
    @Inject(method = "onGameMessage", at=@At("HEAD"))
    private void serverMessageReceived(GameMessageS2CPacket packet, CallbackInfo ci){
        if (!packet.isNonChat()) {
            ShutUp.avoidMessages.add(packet.getMessage().asString());
        }
    }
}
