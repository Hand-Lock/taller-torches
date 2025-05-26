package com.handlock_.tallertorches.mixin.client;

import com.handlock_.tallertorches.TallerTorchesConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WallTorchBlock.class)
public abstract class WallTorchBlockMixin {

    /* ---------------- particelle ---------------- */
    @ModifyArgs(
            method = "randomDisplayTick",
            at     = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void tt$shift(Args args,
                          BlockState state,
                          World world,
                          BlockPos pos,
                          Random random) {

        double yOff    = TallerTorchesConfig.get().offset_y;
        double faceOff = TallerTorchesConfig.get().offset_face;

        args.set(2, ((double) args.get(2)) + yOff);

        Direction d = state.get(WallTorchBlock.FACING);
        switch (d) {
            case NORTH -> args.set(3, ((double) args.get(3)) - faceOff);
            case SOUTH -> args.set(3, ((double) args.get(3)) + faceOff);
            case WEST  -> args.set(1, ((double) args.get(1)) - faceOff);
            case EAST  -> args.set(1, ((double) args.get(1)) + faceOff);
        }
    }

    /* ---------------- hit-box ------------------- */
    @Inject(method = { "getOutlineShape", "getCollisionShape" },
            at = @At("RETURN"), cancellable = true)
    private void tt$stretch(BlockState state,
                            BlockView world,
                            BlockPos pos,
                            ShapeContext ctx,
                            CallbackInfoReturnable<VoxelShape> cir) {

        int    deltaPx  = TallerTorchesConfig.get().torch_height_px - 10;
        double extraY   = deltaPx > 0 ? deltaPx / 16.0 : 0.0;
        double extraXZ  = TallerTorchesConfig.get().offset_face;   // in blocchi

        if (extraY == 0 && extraXZ == 0) return;                   // shape vanilla OK

        Box box = cir.getReturnValue().getBoundingBox();           // limiti vanilla
        double minX = box.minX, maxX = box.maxX;
        double minZ = box.minZ, maxZ = box.maxZ;
        double maxY = box.maxY + extraY;                           // alza la punta

        Direction d = state.get(WallTorchBlock.FACING);
        switch (d) {
            case NORTH -> minZ = Math.max(minZ - extraXZ, 0.0);    // verso -Z
            case SOUTH -> maxZ = Math.min(maxZ + extraXZ, 1.0);    // verso +Z
            case WEST  -> minX = Math.max(minX - extraXZ, 0.0);    // verso -X
            case EAST  -> maxX = Math.min(maxX + extraXZ, 1.0);    // verso +X
        }

        VoxelShape stretched = Block.createCuboidShape(
                minX * 16, box.minY * 16, minZ * 16,
                maxX * 16, maxY  * 16,   maxZ * 16);

        cir.setReturnValue(stretched);
    }
}
