package abyss;

import abyss.act.AbyssAct;
import abyss.act.Encounters;
import abyss.cards.*;
import abyss.events.*;
import abyss.monsters.bosses.DeepTyrant;
import abyss.monsters.bosses.TheCrystal;
import abyss.monsters.bosses.VoidHerald;
import abyss.monsters.bosses.VoidSpawn;
import abyss.monsters.bosses.crystals.*;
import abyss.monsters.elites.Behemoth;
import abyss.monsters.elites.GnawingCorruption;
import abyss.monsters.elites.PrimevalQueen;
import abyss.monsters.normals.*;
import abyss.relics.BehemothsCourage;
import abyss.relics.BehemothsWisdom;
import abyss.relics.CrystalEnergy;
import abyss.relics.HuntersRespect;
import abyss.subscribers.TriggerGrayCrystalPowerPostPowerApplySubscriber;
import abyss.subscribers.TriggerPurpleCrystalPowerPostPowerApplySubscriber;
import abyss.util.TextureLoader;
import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.core.Settings.GameLanguage;
import static com.megacrit.cardcrawl.core.Settings.language;

@SpireInitializer
public class Abyss implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber {
    public static final Logger logger = LogManager.getLogger(Abyss.class.getName());

    public static final String ElementariumModId = "Elementarium";
    public static final String MenagerieModId = "Menagerie";

    public Abyss() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new Abyss();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("abyss/images/AbyssBadge.png");
        BaseMod.registerModBadge(badgeTexture, "Abyss", "modargo", "An alternate act 3 themed around horrors and aberrations. Once where eldritch and primeval beings were sealed away, the Abyss is now at the heart of their reawakening.", new ModPanel());

        CustomDungeon.addAct(AbyssAct.ACT_NUM, new AbyssAct());
        addMonsters();
        addEvents();

        BaseMod.subscribe(new TriggerGrayCrystalPowerPostPowerApplySubscriber());
        BaseMod.subscribe(new TriggerPurpleCrystalPowerPostPowerApplySubscriber());
    }

    private static void addMonsters() {
        //Weak encounters
        BaseMod.addMonster(SquirmingHorror.ID, (BaseMod.GetMonster)SquirmingHorror::new);
        BaseMod.addMonster(Encounters.VICIOUS_HATCHLINGS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new ViciousHatchling(-450.0F, 10.0F, true),
                        new ViciousHatchling(-150.0F, -30.0F, false),
                        new ViciousHatchling(150.0F, 20.0F, true)
                }));

        //Hard encounters
        BaseMod.addMonster(Encounters.VICIOUS_HATCHLINGS_HARD, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new ViciousHatchling(-450.0F, 10.0F, false),
                        new ViciousHatchling(-150.0F, -30.0F, true),
                        new ViciousHatchling(150.0F, 20.0F, false)
                }));
        BaseMod.addMonster(Encounters.CROAKING_TRIO, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new CroakingBrute(-450.0F, 0.0F),
                        new CroakingPelter(-150.0F, 0.0F),
                        new CroakingSeer(150.0F, 0.0F)
                }));
        BaseMod.addMonster(GluttonousAbomination.ID, (BaseMod.GetMonster)GluttonousAbomination::new);
        BaseMod.addMonster(BoundAbyssal.ID, () -> new BoundAbyssal(0, 100.0F));
        BaseMod.addMonster(UnboundAbyssal.ID, (BaseMod.GetMonster)UnboundAbyssal::new);

        //Elites
        BaseMod.addMonster(Behemoth.ID, (BaseMod.GetMonster)Behemoth::new);
        BaseMod.addMonster(GnawingCorruption.ID, (BaseMod.GetMonster)GnawingCorruption::new);
        BaseMod.addMonster(PrimevalQueen.ID, () -> new PrimevalQueen(250.0F, 0.0F));

        //Bosses
        BaseMod.addMonster(Encounters.VOID_HERALD_AND_VOID_SPAWN, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new VoidSpawn(-250.0F, 0.0F),
                        new VoidHerald(150.0F, 0.0F)
                }));
        BaseMod.addBoss(AbyssAct.ID, Encounters.VOID_HERALD_AND_VOID_SPAWN, "abyss/images/map/bosses/Void.png", "abyss/images/map/bosses/VoidOutline.png");
        BaseMod.addMonster(DeepTyrant.ID, () -> new DeepTyrant(150F, -30.0F));
        BaseMod.addBoss(AbyssAct.ID, DeepTyrant.ID, "abyss/images/map/bosses/DeepTyrant.png", "abyss/images/map/bosses/DeepTyrantOutline.png");
        BaseMod.addMonster(Encounters.CRYSTALS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new GoldenCrystal(-350.0F, 400.0F),
                        new BlueCrystal(250.0F, 400.0F),
                        new GreenCrystal(-450.0F, 200.0F),
                        new RedCrystal(350.0F, 200.0F),
                        new PurpleCrystal(-350.0F, 0.0F),
                        new GrayCrystal(250.0F, 0.0F),
                        new TheCrystal(-50.0F, 50.0F)
                }));
        BaseMod.addBoss(AbyssAct.ID, Encounters.CRYSTALS, "abyss/images/map/bosses/Crystals.png", "abyss/images/map/bosses/CrystalsOutline.png");

        //Special fights
        BaseMod.addMonster(Encounters.SQUIRMING_HORRORS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new SquirmingHorror(-250.0F, 0.0F),
                        new SquirmingHorror(150.0F, 0.0F)
                }));
    }

    private static void addEvents() {
        // Mind Bloom is critical enough to how act 3 is played that we keep it around
        BaseMod.addEvent(MindBloom.ID, MindBloom.class, AbyssAct.ID);

        BaseMod.addEvent(AbyssalSphere.ID, AbyssalSphere.class, AbyssAct.ID);
        BaseMod.addEvent(CorruptedStone.ID, CorruptedStone.class, AbyssAct.ID);
        BaseMod.addEvent(ElderBehemoth.ID, ElderBehemoth.class, AbyssAct.ID);
        BaseMod.addEvent(HungryVoid.ID, HungryVoid.class, AbyssAct.ID);
        BaseMod.addEvent(Humming.ID, Humming.class, AbyssAct.ID);
        BaseMod.addEvent(SpawningGrounds.ID, SpawningGrounds.class, AbyssAct.ID);
        BaseMod.addEvent(BigGameHunter.ID, BigGameHunter.class, AbyssAct.ID);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new HandOfTheAbyss());

        //Curses and statuses
        BaseMod.addCard(new BrokenCrystal());
        BaseMod.addCard(new Drained());
        BaseMod.addCard(new Panic());
        BaseMod.addCard(new Staggered());
        BaseMod.addCard(new Tormented());
        BaseMod.addCard(new Withering());
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new BehemothsCourage(), RelicType.SHARED);
        BaseMod.addRelic(new BehemothsWisdom(), RelicType.SHARED);
        BaseMod.addRelic(new CrystalEnergy(), RelicType.SHARED);
        BaseMod.addRelic(new HuntersRespect(), RelicType.SHARED);
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            default:
                ret += "eng";
                break;
        }
        return "abyss/" + ret + "/" + filename + ".json";
    }

    private void loadLocFiles(GameLanguage language)
    {
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(language, "Abyss-Card-Strings"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(language, "Abyss-Event-Strings"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, makeLocPath(language, "Abyss-Monster-Strings"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(language, "Abyss-Relic-Strings"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(language, "Abyss-Power-Strings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(language, "Abyss-ui"));
    }

    @Override
    public void receiveEditStrings()
    {
        loadLocFiles(GameLanguage.ENG);
        if (language != GameLanguage.ENG) {
            loadLocFiles(language);
        }
    }


    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(makeLocPath(Settings.language, "Abyss-Keyword-Strings")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                //The modID here must be lowercase
                BaseMod.addKeyword("abyss", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String cardImage(String id) {
        return "abyss/images/cards/" + removeModId(id) + ".png";
    }
    public static String eventImage(String id) {
        return "abyss/images/events/" + removeModId(id) + ".png";
    }
    public static String relicImage(String id) {
        return "abyss/images/relics/" + removeModId(id) + ".png";
    }
    public static String powerImage32(String id) {
        return "abyss/images/powers/" + removeModId(id) + "32.png";
    }
    public static String powerImage84(String id) {
        return "abyss/images/powers/" + removeModId(id) + "84.png";
    }
    public static String monsterImage(String id) {
        return "abyss/images/monsters/" + removeModId(id) + "/" + removeModId(id) + ".png";
    }
    public static String relicOutlineImage(String id) {
        return "abyss/images/relics/outline/" + removeModId(id) + ".png";
    }

    public static String removeModId(String id) {
        if (id.startsWith("Abyss:")) {
            return id.substring(id.indexOf(':') + 1);
        } else {
            logger.warn("Missing mod id on: " + id);
            return id;
        }
    }

    public static void LoadPowerImage(AbstractPower power) {
        Texture tex84 = TextureLoader.getTexture(Abyss.powerImage84(power.ID));
        Texture tex32 = TextureLoader.getTexture(Abyss.powerImage32(power.ID));
        power.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        power.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    private static class Keyword
    {
        public String PROPER_NAME;
        public String[] NAMES;
        public String DESCRIPTION;
    }
}