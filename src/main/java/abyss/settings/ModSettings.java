package abyss.settings;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModToggleButton;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.util.Properties;

public class ModSettings {
    private static Properties DEFAULT_SETTINGS = new Properties();

    private static SpireConfig config;

    public static void initialize() {
        try {
            config = new SpireConfig("Abyss", "Abyss", DEFAULT_SETTINGS);
            config.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModPanel getModPanel() {
        ModPanel panel = new ModPanel();
        return panel;
    }
}
