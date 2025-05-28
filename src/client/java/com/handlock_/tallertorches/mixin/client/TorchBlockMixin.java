/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2025 HandLock_
 */

package com.handlock_.tallertorches.mixin.client;

import com.handlock_.tallertorches.TallerTorchesConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixiamo solo TorchBlock (le redstone ereditano).
 */
@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin {

	/*────────── particelle ─────────────────────────────────────────────*/
	@ModifyArg(
			method = "randomDisplayTick",
			at     = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;addParticle"
							+ "(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"),
			index  = 2)
	private double tt$raiseY(double y) {
		if ((Object) this instanceof RedstoneTorchBlock           // redstone?
				&& !TallerTorchesConfig.get().include_redstone)
			return y;                       // lascia vanilla

		return y + TallerTorchesConfig.get().offset_y;
	}

	/*────────── hit-box / outline ──────────────────────────────────────*/
	@Inject(method = { "getOutlineShape", "getCollisionShape" },
			at = @At("HEAD"), cancellable = true)
	private void tt$shape(BlockState state,
						  BlockView world,
						  BlockPos pos,
						  ShapeContext ctx,
						  CallbackInfoReturnable<VoxelShape> cir) {

		if ((Object) this instanceof RedstoneTorchBlock
				&& !TallerTorchesConfig.get().include_redstone)
			return;                          // shape vanilla

		int deltaPx = TallerTorchesConfig.get().torch_height_px - 10;
		if (deltaPx <= 0) return;            // vanilla OK

		double top = 10 + deltaPx;           // maxY in pixel
		VoxelShape shape = Block.createCuboidShape(6, 0, 6, 10, top, 10);
		cir.setReturnValue(shape);
	}
}
