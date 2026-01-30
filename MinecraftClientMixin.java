package dev.drinz.airplaceopti.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public ClientPlayerEntity player;
    @Shadow public ClientWorld world;
    @Shadow public HitResult crosshairTarget;
    @Shadow @Final public ClientPlayerInteractionManager interactionManager;

    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void onItemUse(CallbackInfo info) {
        if(this.world != null) {
            if (this.crosshairTarget instanceof BlockHitResult hit && this.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)) {
                Block Block = this.world.getBlockState(hit.getBlockPos()).getBlock();
                if (Block == Blocks.RESPAWN_ANCHOR && this.world.getBlockState(hit.getBlockPos()).get(RespawnAnchorBlock.CHARGES) != 0) {
                    ActionResult result = this.interactionManager.interactBlock(this.player, Hand.MAIN_HAND, hit);
                    if (result.isAccepted()) {
                        this.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
