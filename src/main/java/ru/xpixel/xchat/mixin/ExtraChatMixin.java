package ru.xpixel.xchat.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xpixel.xchat.ExtraChatMod;

@Mixin(TitleScreen.class)
public class ExtraChatMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		ExtraChatMod.LOGGER.info("This line is printed by an example mod mixin!");
	}
}
