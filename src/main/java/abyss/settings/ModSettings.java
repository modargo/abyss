package abyss.settings;

import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

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
