package com.handlock_.tallertorches.mixin.client;

import net.minecraft.block.TorchBlock;            // <- classe corretta
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin {
	@Redirect(
			method = "randomDisplayTick",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;" +
							"DDDDDD)V")
	)
	private void tallerTorches$raiseStanding(
			World world, ParticleEffect effect,
			double x, double y, double z,
			double vx, double vy, double vz) {

		world.addParticle(effect, x, y + 0.1875D, z, vx, vy, vz);   // +3 px
	}
}
