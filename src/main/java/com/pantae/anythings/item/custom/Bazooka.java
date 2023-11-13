package com.pantae.anythings.item.custom;

import com.pantae.anythings.Main;
import com.pantae.anythings.entity.custom.ExplodingProjectile;
import com.pantae.anythings.enums.ModItemEnums;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Bazooka extends ProjectileWeaponItem {
    public Bazooka() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!pLevel.isClientSide()) {
            if (pLivingEntity instanceof Player pPlayer) {
                for (ItemStack itemStack : pPlayer.getInventory().items) {
                    if (itemStack.getItem() == Main.ITEMS.MAP.get(ModItemEnums.EXPLOSION_ITEM).get() || pPlayer.getAbilities().instabuild) {
                        if (itemStack.getCount() < 32 && !pPlayer.getAbilities().instabuild) break;
                        if (pTimeCharged <= 90) {
                            if (!pPlayer.getAbilities().instabuild) itemStack.shrink(32);
                            ExplodingProjectile i = ExplodingProjectile.shoot(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, (float) (100 - pTimeCharged) / 20 * 2F,
                                    1.0F, pPlayer.getEyePosition(), pLevel);
                            pLevel.addFreshEntity(i);
                            break;
                        }
                    }
                }
            }
        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Consume 32 explosion item per shot."));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack -> itemStack.equals(new ItemStack(Main.ITEMS.MAP.get(ModItemEnums.EXPLOSION_ITEM).get())));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
}
