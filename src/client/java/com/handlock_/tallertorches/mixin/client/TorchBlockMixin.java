package com.handlock_.tallertorches.mixin.client;

import com.handlock_.tallertorches.TallerTorchesConfig;
import net.minecraft.block.TorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin {
	@ModifyArg(
			method = "randomDisplayTick",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"),
			index = 2   // y
	)
	private double tt$raiseY(double y) {
		return y + TallerTorchesConfig.get().offset_y;
	}
}
