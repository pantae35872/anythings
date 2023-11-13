package com.pantae.anythings.item.custom;

import com.mojang.brigadier.Command;
import com.pantae.anythings.Main;
import com.pantae.anythings.helper.ExplosionHelper;
import com.pantae.anythings.networking.ModNetwork;
import com.pantae.anythings.networking.packet.TestC2SPacket;
import com.pantae.anythings.networking.packet.TestS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ExplosionItem extends Item {
    public ExplosionItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        ExplosionHelper.FallingBlockExplosion(new BlockPos(pPlayer.getBlockX(), pPlayer.getBlockY(), pPlayer.getBlockZ()), pLevel, itemInHand.getCount() / 8, itemInHand.getCount());
        if (!pPlayer.getAbilities().instabuild) {
            return InteractionResultHolder.consume(new ItemStack(Items.AIR));
        } else {
            return InteractionResultHolder.pass(itemInHand);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("DO NOT RIGHT CLICK").withStyle(ChatFormatting.RED));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
