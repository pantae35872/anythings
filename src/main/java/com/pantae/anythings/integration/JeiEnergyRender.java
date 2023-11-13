package com.pantae.anythings.integration;

import com.pantae.anythings.Main;
import com.pantae.anythings.util.ModEnergyStorage;
import com.pantae.anythings.util.MouseUtil;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JeiEnergyRender {
    public JeiEnergyRender(int amount, int maxEnergy) {
        this.energy = amount;
        this.maxEnergy = maxEnergy;
    }
    public void draw(GuiGraphics guiGraphics, double mouseX, double mouseY, int width, int height, int imageWidth, int imageHeight) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.renderEnergyAreaTooltips(guiGraphics, (int)mouseX, (int)mouseY, x, y);
        this.area = new Rect2i(148, 10, 16, 64);
        this.drawEnergy(guiGraphics);
    }
    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(pMouseX, pMouseY, x, y, 148, 9, 15, 63)) {
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
    protected Rect2i area;
    public int energy;
    public int maxEnergy;
    public List<Component> getTooltips() {
        return List.of(Component.literal(energy+"/"+maxEnergy+" FE"));
    }
    public void drawEnergy(GuiGraphics guiGraphics) {
        final int height = area.getHeight();
        int stored = (int) (height*(energy/(float)maxEnergy));
        guiGraphics.fillGradient(area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                area.getX(), area.getY()-(stored-height),
                0xff600b00, 0xffb51500
        );
    }
}
