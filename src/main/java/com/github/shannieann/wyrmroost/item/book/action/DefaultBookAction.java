package com.github.shannieann.wyrmroost.item.book.action;

import com.github.shannieann.wyrmroost.events.ClientEvents;
import com.github.shannieann.wyrmroost.client.render.RenderHelper;
import com.github.shannieann.wyrmroost.containers.NewTarragonTomeContainer;
import com.github.shannieann.wyrmroost.entity.dragon.WRDragonEntity;
import com.github.shannieann.wyrmroost.item.book.TarragonTomeItem;
import com.github.shannieann.wyrmroost.util.WRMathsUtility;
import com.github.shannieann.wyrmroost.util.WRModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;


public class DefaultBookAction implements BookAction
{
    @Override
    public InteractionResult rightClick(@Nullable WRDragonEntity dragon, Player player, ItemStack stack)
    {
        boolean client = player.getLevel().isClientSide();
        if (dragon != null && !client)
        {
            NewTarragonTomeContainer.open((ServerPlayer) player, dragon);


        }
        else if ((dragon = clip(player)) != null)
        {
            TarragonTomeItem.bind(dragon, stack);
            if (client)
            {
                WRModUtils.playLocalSound(player.getLevel(), player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.75f, 2f);
                WRModUtils.playLocalSound(player.getLevel(), player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 0.75f, 1f);
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void render(@Nullable WRDragonEntity dragon, PoseStack ms, float partialTicks)
    {
        if (dragon == null && (dragon = clip(ClientEvents.getPlayer())) != null)
            RenderHelper.renderEntityOutline(dragon,
                    255,
                    255,
                    255,
                    (int) (Mth.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
    }

    @Nullable
    private WRDragonEntity clip(Player player)
    {
        EntityHitResult ertr = WRMathsUtility.clipEntities(player, 40, 0.75, e -> e instanceof WRDragonEntity && ((WRDragonEntity) e).isOwnedBy(player));
        return ertr != null? (WRDragonEntity) ertr.getEntity() : null;
    }

    @Override
    public String getTranslateKey(@Nullable WRDragonEntity dragon)
    {
        return TRANSLATE_PATH + "default";
    }
}
