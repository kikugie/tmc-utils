package me.kikugie.tmcutils.mixin;

import me.kikugie.tmcutils.util.ResponseMuffler;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public class MessageHandlerMixin {
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void interceptMessage(MessageType messageType, Text message, UUID sender, CallbackInfo ci) {
        if (ResponseMuffler.matches(message.getString())) {
            ResponseMuffler.pop();
            ci.cancel();
        }
    }
}
