package com.pantae.anythings.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pantae.anythings.Main;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import com.pantae.anythings.networking.ModNetwork;
import com.pantae.anythings.networking.packet.ExplosionFuelGeneratorScreenStateC2SPacket;
import com.pantae.anythings.screen.customwidgets.SideConfigWidget;
import com.pantae.anythings.util.MouseUtil;
import mezz.jei.api.JeiPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Optional;

public class ExplosionFuelGeneratorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/explosion_fuel_generator.png");
    private static final Component SIDE_BUTTON = Component.literal("change");
    private SideConfigWidget sideConfig;

    public ExplosionFuelGeneratorScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void onClose() {
        ModNetwork.sendToServer(new ExplosionFuelGeneratorScreenStateC2SPacket(((ExplosionFuelGeneratorMenu) menu).blockEntity.getBlockPos()
                ,false, ((ExplosionFuelGeneratorMenu) menu).inv.player.getUUID()));
        super.onClose();
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.energy = ((ExplosionFuelGeneratorMenu) menu).blockEntity.getEnergyStorage();
        this.area = new Rect2i(x + 152, y +13, 16, 64);
        this.sideConfig = addRenderableWidget(new SideConfigWidget(x + 5, y + 5, 64, 64, SIDE_BUTTON));
    }

    private void handleSidePress(Button button) {
        Main.LOGGER.debug("pressed");
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgress(pGuiGraphics, x, y);
        this.drawEnergy(pGuiGraphics);
    }


    private void renderProgress(GuiGraphics pGuiGraphics, int x, int y) {
        if (((ExplosionFuelGeneratorMenu) menu).isCrafting()) {
            pGuiGraphics.blit(TEXTURE, x + 106, y + 33, 176, 0, ((ExplosionFuelGeneratorMenu) menu).getScaledProgress(), 18);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        {
            renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pGuiGraphics, pMouseX, pMouseY, x,y);
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(pMouseX, pMouseY, x, y, 152, 13, 15, 63)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, getTooltips(), Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    /*
     *  BluSunrize
     *  Copyright (c) 2021
     *
     *  This code is licensed under "Blu's License of Common Sense"
     *  Details can be found in the license file in the root folder of this project
     */
    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" FE"));
    }
    protected Rect2i area;
    protected IEnergyStorage energy;
    public void drawEnergy(GuiGraphics guiGraphics) {
        final int height = area.getHeight();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        guiGraphics.fillGradient(area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xffb51500, 0xff600b00
        );
    }
}