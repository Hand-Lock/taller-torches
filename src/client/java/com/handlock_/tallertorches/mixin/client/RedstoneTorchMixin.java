/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2025 HandLock_
 */

package com.handlock_.tallertorches.mixin.client;

import com.handlock_.tallertorches.TallerTorchesConfig;
import net.minecraft.block.RedstoneTorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Alza la particella della redstone-torch verticale. */
@Mixin(RedstoneTorchBlock.class)
public abstract class RedstoneTorchMixin {

    @ModifyArg(
            method = "randomDisplayTick",
            at     = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle"
                            + "(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"),
            index  = 2)
    private double tt$liftY(double y) {
        return TallerTorchesConfig.get().include_redstone
                ? y + TallerTorchesConfig.get().offset_y
                : y;
    }
}
