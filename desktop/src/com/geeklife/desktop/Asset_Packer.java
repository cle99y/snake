package com.geeklife.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by cle99 on 26/05/2017.
 */

public class Asset_Packer {

    private static final String RAW_ASSETS_PATH = "desktop/assets-raw";
    private static final String ASSETS_PATH = "android/assets";

    public static void main( String[] args ) {
        TexturePacker.Settings settings = new TexturePacker.Settings();

        TexturePacker.process( settings,
                RAW_ASSETS_PATH + "/gameplay",
                ASSETS_PATH + "/gameplay",
                "gameplay"
        );

        TexturePacker.process( settings,
                RAW_ASSETS_PATH + "/ui",
                ASSETS_PATH + "/ui",
                "ui"
        );

    }


}
