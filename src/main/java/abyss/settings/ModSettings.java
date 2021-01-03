package abyss.settings;

import basemod.ModLabel;
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
    private static final String ACT_4_ELITE_SETTING = "act_4_elite";
    static {
        DEFAULT_SETTINGS.setProperty(ACT_4_ELITE_SETTING, "true");
    }

    private static SpireConfig config;

    public static void initialize() {
        try {
            config = new SpireConfig("Abyss", "Abyss", DEFAULT_SETTINGS);
            config.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAct4EliteEnabled() {
        return config.getBool(ACT_4_ELITE_SETTING);
    }

    public static void onAct4EliteToggle(ModToggleButton toggle) {
        try {
            config.setBool(ACT_4_ELITE_SETTING, toggle.enabled);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModPanel getModPanel() {
        String[] MOD_PANEL_TEXT = CardCrawlGame.languagePack.getUIString("Abyss:ModSettingsPanel").TEXT;
        ModPanel panel = new ModPanel();

        ModLabeledToggleButton act4EliteToggleButton = new ModLabeledToggleButton(
                MOD_PANEL_TEXT[0],
                400.0F,
                740.0F,
                Color.WHITE,
                FontHelper.tipHeaderFont,
                isAct4EliteEnabled(),
                panel,
                (label) -> {},
                ModSettings::onAct4EliteToggle);
        panel.addUIElement(act4EliteToggleButton);
        return panel;
    }
}
