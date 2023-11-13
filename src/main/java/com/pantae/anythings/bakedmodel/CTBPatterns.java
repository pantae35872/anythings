package com.pantae.anythings.bakedmodel;

import com.pantae.anythings.enums.ModConnectedTextureEnum;

import java.util.HashMap;
import java.util.Map;

import static com.pantae.anythings.enums.ModConnectedTextureEnum.NONE;

public class CTBPatterns {
    public enum SpriteIdx {
        SPRITE_NONE,
        SPRITE_END,
        SPRITE_STRAIGHT,
        SPRITE_CORNER,
        SPRITE_THREE,
    }
    public record QuadSetting(SpriteIdx sprite, int rotation) {

        public static QuadSetting of(SpriteIdx sprite, int rotation) {
            return new QuadSetting(sprite, rotation);
        }
    }
    public record Pattern(boolean s1, boolean s2, boolean s3, boolean s4) {

        public static Pattern of(boolean s1, boolean s2, boolean s3, boolean s4) {
            return new Pattern(s1, s2, s3, s4);
        }
    }
}
