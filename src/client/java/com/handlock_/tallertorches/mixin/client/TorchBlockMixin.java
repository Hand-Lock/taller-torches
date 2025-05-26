package com.handlock_.tallertorches.mixin.client;

import net.minecraft.block.TorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin {

	private static final double OFFSET = 0.1875D;   // 3 pixel = 3/16 blocco

	@ModifyArg(
			method = "randomDisplayTick",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"),
			index = 2   // l’argomento Y
	)
	private double tallerTorches$raiseY(double y) {
		return y + OFFSET;
	}
}
