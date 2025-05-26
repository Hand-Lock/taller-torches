package com.handlock_.tallertorches.mixin.client;

import com.handlock_.tallertorches.TallerTorchesConfig;
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

        double yOff   = TallerTorchesConfig.get().offset_y;
        double faceOff= TallerTorchesConfig.get().offset_face;

        // 0 = effect, 1 = x, 2 = y, 3 = z
        args.set(2, ((double) args.get(2)) + yOff);

        Direction dir = state.get(WallTorchBlock.FACING);
        switch (dir) {
            case NORTH -> args.set(3, ((double) args.get(3)) - faceOff);
            case SOUTH -> args.set(3, ((double) args.get(3)) + faceOff);
            case WEST  -> args.set(1, ((double) args.get(1)) - faceOff);
            case EAST  -> args.set(1, ((double) args.get(1)) + faceOff);
        }
    }
}
