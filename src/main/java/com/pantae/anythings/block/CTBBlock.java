package com.pantae.anythings.block;

import com.pantae.anythings.enums.ModConnectedTextureEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CTBBlock extends Block {
    public static EnumProperty<ModConnectedTextureEnum> NORTH = EnumProperty.create("north", ModConnectedTextureEnum.class);
    public static EnumProperty<ModConnectedTextureEnum> SOUTH = EnumProperty.create("south", ModConnectedTextureEnum.class);
    public static EnumProperty<ModConnectedTextureEnum> EAST = EnumProperty.create("east", ModConnectedTextureEnum.class);
    public static EnumProperty<ModConnectedTextureEnum> WEST = EnumProperty.create("west", ModConnectedTextureEnum.class);
    public static EnumProperty<ModConnectedTextureEnum> UP = EnumProperty.create("up", ModConnectedTextureEnum.class);
    public static EnumProperty<ModConnectedTextureEnum> DOWN = EnumProperty.create("down", ModConnectedTextureEnum.class);
    public CTBBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(NORTH, ModConnectedTextureEnum.SIDE)
                .setValue(SOUTH, ModConnectedTextureEnum.SIDE)
                .setValue(EAST, ModConnectedTextureEnum.SIDE)
                .setValue(WEST, ModConnectedTextureEnum.SIDE)
                .setValue(UP, ModConnectedTextureEnum.SIDE)
                .setValue(DOWN, ModConnectedTextureEnum.SIDE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return getConnectedState(pContext.getLevel(), this.defaultBlockState(), pContext.getClickedPos());
    }

    private BlockState getConnectedState(Level level, BlockState state, BlockPos pos) {
        if (level.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == this.asBlock()) {
            state = state.setValue(UP, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(UP, ModConnectedTextureEnum.SIDE);
        }
        if (level.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == this.asBlock()) {
            state = state.setValue(DOWN, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(DOWN, ModConnectedTextureEnum.SIDE);
        }
        if (level.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getBlock() == this.asBlock()) {
            state = state.setValue(WEST, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(WEST, ModConnectedTextureEnum.SIDE);
        }
        if (level.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getBlock() == this.asBlock()) {
            state = state.setValue(EAST, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(EAST, ModConnectedTextureEnum.SIDE);
        }
        if (level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getBlock() == this.asBlock()) {
            state = state.setValue(SOUTH, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(SOUTH, ModConnectedTextureEnum.SIDE);
        }
        if (level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getBlock() == this.asBlock()) {
            state = state.setValue(NORTH, ModConnectedTextureEnum.NONE);
        } else {
            state = state.setValue(NORTH, ModConnectedTextureEnum.SIDE);
        }

        return state;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        pLevel.setBlock(pCurrentPos, getConnectedState((Level) pLevel, pState, pCurrentPos), 3);
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }
}
