package com.handlock_.tallertorches.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WallTorchBlock.class)
public abstract class WallTorchBlockMixin {

    private static final double OFFSET_Y    = 0.1875D; // +3 px
    private static final double OFFSET_FACE = 0.0625D; // +1 px dalla parete

    @ModifyArgs(
            method = "randomDisplayTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V")
    )
    private void tallerTorches$shift(Args args,
                                     BlockState state,
                                     World world,
                                     BlockPos pos,
                                     Random random) {

        /* Indici in Args:
           0 = ParticleEffect
           1 = x
           2 = y
           3 = z
        */

        // Alza la particella
        args.set(2, ((double) args.get(2)) + OFFSET_Y);

        // Sposta 1 px fuori dal muro
        Direction dir = state.get(WallTorchBlock.FACING);
        switch (dir) {
            case NORTH -> args.set(3, ((double) args.get(3)) - OFFSET_FACE); // z–
            case SOUTH -> args.set(3, ((double) args.get(3)) + OFFSET_FACE); // z+
            case WEST  -> args.set(1, ((double) args.get(1)) - OFFSET_FACE); // x–
            case EAST  -> args.set(1, ((double) args.get(1)) + OFFSET_FACE); // x+
        }
    }
}
