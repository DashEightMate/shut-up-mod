package com.DashEightMate.shutup.mixin;

import com.DashEightMate.shutup.ShutUp;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LevelLoadingScreen.class)
public class LoadingScreenMixin extends Screen{
    protected LoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method="render", at=@At("TAIL"))
    private void drawOverrideStatus(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        ShutUp.isLoading = true;

        if (!ShutUp.overrideBinding.isUnbound() && InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
                        KeyBindingHelper.getBoundKeyOf(ShutUp.overrideBinding).getCode())){
            ShutUp.isOverridden = true;
        }

        String overrideText;
        if (ShutUp.isOverridden) overrideText = "Showing messages at start...";
        else overrideText = "["+((StringVisitable)new TranslatableText(ShutUp.overrideBinding.getBoundKeyTranslationKey())).getString()
                +"] Show startup messages";
        textRenderer.draw(matrices, overrideText, width-textRenderer.getWidth(overrideText)-5, 5,
                ShutUp.isOverridden ? 16766054 : 16777215);
    }
}
