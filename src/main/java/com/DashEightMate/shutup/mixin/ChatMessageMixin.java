package com.DashEightMate.shutup.mixin;

import com.DashEightMate.shutup.ShutUp;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatHud.class)
public class ChatMessageMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at=@At("HEAD"), cancellable = true)
    private void messageIntercept(Text message, int messageId, CallbackInfo ci){
        if (ShutUp.isBlocking){
            if (ShutUp.avoidMessages.contains(message.asString())){
                ShutUp.avoidMessages.remove(message.asString());
            } else {
                ShutUp.numberBlocked++;
                ci.cancel();
            }
        }
    }
}
