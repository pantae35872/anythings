package com.pantae.anythings.block.entity;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModBlockEntityEnum;
import com.pantae.anythings.networking.ModNetwork;
import com.pantae.anythings.networking.packet.*;
import com.pantae.anythings.recipe.ExplosionFuelGeneratorRecipe;
import com.pantae.anythings.screen.ExplosionFuelGeneratorMenu;
import com.pantae.anythings.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ExplosionFuelGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> level.getRecipeManager().getAllRecipesFor(
                        ExplosionFuelGeneratorRecipe.Type.INSTANCE).stream().anyMatch(recipe -> recipe.value().matches(new SimpleContainer(stack), level));
                case 1 -> stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(1000000, 100000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }
    };
    public final AnimationState running = new AnimationState();
    public final AnimationState open = new AnimationState();
    public final AnimationState close = new AnimationState();
    public int tickCount = 0;

    protected final ContainerData data;
    public Map<UUID, Boolean> client_screen_state = new ConcurrentHashMap<>();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private int progress = 0;
    private ItemStack latestFuel = ItemStack.EMPTY;
    private int latestFuelPower = 0;
    private int latestFuelGeneratingFactor = 0;
    private boolean isRunning = false;
    private boolean animate = false;

    public ExplosionFuelGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Main.BLOCK_ENTITIES.getRegistry(ModBlockEntityEnum.ExplosionFuelGenerator).get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ExplosionFuelGeneratorBlockEntity.this.progress;
                    case 1 -> ExplosionFuelGeneratorBlockEntity.this.latestFuelPower;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> ExplosionFuelGeneratorBlockEntity.this.progress = pValue;
                    case 1 -> ExplosionFuelGeneratorBlockEntity.this.latestFuelPower = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Explosion Fuel Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModNetwork.sendToClient(new ExplosionFuelGeneratorBlockPosS2CPacket(this.getBlockPos()), PacketDistributor.PLAYER.with((ServerPlayer) pPlayer));
        sendOpenState();
        addScreenState(true, pPlayer.getUUID());
        return new ExplosionFuelGeneratorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return  lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        ItemStackHandler tempItemStack = new ItemStackHandler(1);
        tempItemStack.insertItem(0, this.latestFuel, false);
        pTag.put("inventory", itemStackHandler.serializeNBT());
        pTag.putInt("energy", this.ENERGY_STORAGE.getEnergyStored());
        pTag.putInt("progress", this.progress);
        pTag.putInt("latest_fuel_power", this.latestFuelPower);
        pTag.put("latest_fuel", tempItemStack.serializeNBT());
        pTag.putBoolean("is_running", this.isRunning);
        pTag.putInt("latest_fuel_generating_factor", this.latestFuelGeneratingFactor);
        pTag.putBoolean("animate", this.animate);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ItemStackHandler tempItemStack = new ItemStackHandler(1);
        tempItemStack.deserializeNBT(pTag.getCompound("latest_fuel"));
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(pTag.getInt("energy"));
        this.progress = pTag.getInt("progress");
        this.latestFuelPower = pTag.getInt("latest_fuel_power");
        this.latestFuel.deserializeNBT(pTag.getCompound("latest_fuel"));
        this.isRunning = pTag.getBoolean("is_running");
        this.latestFuel = tempItemStack.getStackInSlot(0);
        this.latestFuelGeneratingFactor = pTag.getInt("latest_fuel_generating_factor");
        this.animate = pTag.getBoolean("animate");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i =0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(Objects.requireNonNull(this.getLevel()), this.getBlockPos(), inventory);
    }

    public void tick(Level level, BlockPos blockPos, BlockState state) {
        tickCount++;
        if (level.isClientSide()) {
            return;
        }
        if (tickCount % 20 == 0)
            ModNetwork.sendToClient(new ExplosionFuelGeneratorAnimationSyncS2C(this.animate, this.getBlockPos()), PacketDistributor.ALL.noArg());
        ModNetwork.sendToClient(new ExplosionFuelGeneratorSyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos(),
                        this.latestFuel),
                    PacketDistributor.ALL.noArg());
        ItemStack fuelStack = this.itemStackHandler.getStackInSlot(0);
        ItemStack batteryStack = this.itemStackHandler.getStackInSlot(1);
        AtomicInteger fuelPower = new AtomicInteger();
        AtomicInteger fuelGeneratingFactor = new AtomicInteger();
        fuelPower.set(0);
        fuelGeneratingFactor.set(0);
        List<RecipeHolder<ExplosionFuelGeneratorRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ExplosionFuelGeneratorRecipe.Type.INSTANCE);
        if (batteryStack.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            if (batteryStack.getCapability(ForgeCapabilities.ENERGY).resolve().isPresent()) {
                IEnergyStorage energyStorage = batteryStack.getCapability(ForgeCapabilities.ENERGY).resolve().get();
                int extractEnergy = this.ENERGY_STORAGE.extractEnergy(100000, false);
                int energyStorageExtract = energyStorage.receiveEnergy(extractEnergy, false);
                this.ENERGY_STORAGE.privateReceiveEnergy(extractEnergy - energyStorageExtract, false);
            }
        }
        if (!this.isRunning) {
            if (recipes.stream().anyMatch(recipe -> recipe.value().matches(new SimpleContainer(fuelStack), level))) {
                recipes.forEach(recipe -> {
                    if (fuelPower.get() == 0) {
                        fuelPower.set(recipe.value().getPower(fuelStack).get(0));
                        fuelGeneratingFactor.set(recipe.value().getPower(fuelStack).get(1));
                    }
                });
                this.latestFuel = new ItemStack(fuelStack.getItem());
                this.latestFuelPower = fuelPower.get();
                fuelStack.shrink(1);
                this.isRunning = true;
                this.latestFuelGeneratingFactor = fuelGeneratingFactor.get();
            }
        } else {
            if (!(this.ENERGY_STORAGE.getEnergyStored() >= this.ENERGY_STORAGE.getMaxEnergyStored())) {
                this.progress++;
                this.ENERGY_STORAGE.privateReceiveEnergy(this.latestFuelPower * this.latestFuelGeneratingFactor, false);
                this.animate = true;
                if (this.progress >= this.latestFuelPower) {
                    this.latestFuelPower = 0;
                    this.latestFuel = ItemStack.EMPTY;
                    this.progress = 0;
                    this.isRunning = false;
                    this.animate = false;
                    this.level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, Level.ExplosionInteraction.BLOCK);
                }
            } else this.animate = false;
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    public ItemStack getRenderStack() {
        return this.latestFuel;
    }
    public void setLatestFuel(ItemStack stack) {
        this.latestFuel = stack;
    }

    public void addScreenState(boolean newState, UUID player) {
        if (!this.client_screen_state.containsKey(player)) {
            this.client_screen_state.put(player, newState);
        } else {
            this.client_screen_state.replace(player, newState);
        }
        updateScreenState();
    }

    public void updateScreenState() {
        AtomicBoolean send = new AtomicBoolean();
        send.set(true);
        this.client_screen_state.forEach((key, value) -> {
            if (value) send.set(false);
        });
        if (send.get()) ModNetwork.sendToClient(new ExplosionFuelGeneratorOpenCloseS2CSyncPacket(getBlockPos(), false, true),
                PacketDistributor.ALL.noArg());
    }

    public void sendOpenState() {
        AtomicBoolean send = new AtomicBoolean();
        send.set(true);
        this.client_screen_state.forEach((key, value) -> {
            if (value) send.set(false);
        });
        if (send.get()) ModNetwork.sendToClient(new ExplosionFuelGeneratorOpenCloseS2CSyncPacket(getBlockPos(), true, false),
                PacketDistributor.ALL.noArg());
    }
}
