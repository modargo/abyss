package abyss;

import abyss.act.AbyssAct;
import abyss.act.Encounters;
import abyss.act.VoidAct;
import abyss.cards.*;
import abyss.events.*;
import abyss.monsters.act4.AnnihilationMage;
import abyss.monsters.act4.AnnihilationWarrior;
import abyss.monsters.act4.UniversalVoid;
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
import abyss.settings.ModSettings;
import abyss.subscribers.TriggerDrainedPostExhaustSubscriber;
import abyss.subscribers.TriggerGrayCrystalPowerPostPowerApplySubscriber;
import abyss.subscribers.TriggerRedCrystalPowerPostExhaustSubscriber;
import abyss.subscribers.TriggerThoughtStealerPostDrawSubscriber;
import abyss.util.TextureLoader;
import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
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
        logger.info("Adding mod settings");
        ModSettings.initialize();
    }

    public static void initialize() {
        new Abyss();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("abyss/images/AbyssBadge.png");
        BaseMod.registerModBadge(badgeTexture, "Abyss", "modargo", "An alternate act 3 themed around horrors and aberrations. Once where eldritch and primeval beings were sealed away, the Abyss is now at the heart of their reawakening.", ModSettings.getModPanel());

        CustomDungeon.addAct(AbyssAct.ACT_NUM, new AbyssAct());
        CustomDungeon.addAct(VoidAct.ACT_NUM, new VoidAct());
        addMonsters();
        addEvents();

        BaseMod.subscribe(new TriggerGrayCrystalPowerPostPowerApplySubscriber());
        BaseMod.subscribe(new TriggerThoughtStealerPostDrawSubscriber());
        BaseMod.subscribe(new TriggerDrainedPostExhaustSubscriber());
        BaseMod.subscribe(new TriggerRedCrystalPowerPostExhaustSubscriber());
    }

    private static void addMonsters() {
        //Weak encounters
        BaseMod.addMonster(SquirmingHorror.ID, (BaseMod.GetMonster)SquirmingHorror::new);
        BaseMod.addMonster(Encounters.HATCHLINGS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-450.0F, 10.0F, true),
                        new Hatchling(-150.0F, -30.0F, false),
                        new Hatchling(150.0F, 20.0F, true)
                }));
        BaseMod.addMonster(Encounters.DEMOLISHER_AND_HUNTERS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hunter(-450.0F, -10.0F, true),
                        new Hunter(-150.0F, 10.0F, false),
                        new Demolisher(150.0F, 100.0F)
                }));

        //Hard encounters
        BaseMod.addMonster(Encounters.HATCHLINGS_HARD, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-450.0F, 10.0F, false),
                        new Hatchling(-150.0F, -30.0F, true),
                        new Hatchling(150.0F, 20.0F, false)
                }));
        BaseMod.addMonster(Encounters.DEMOLISHER_HUNTER_AND_HATCHLING, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-450.0F, -10.0F, false),
                        new Hunter(-150.0F, 10.0F, false),
                        new Demolisher(150.0F, 100.0F)
                }));
        BaseMod.addMonster(Encounters.SQUIRMING_HORROR_AND_HATCHLING, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-300.0F, 0.0F, false),
                        new SquirmingHorror(100.0F, 0.0F)
                }));
        BaseMod.addMonster(Encounters.ESSENCE_THIEF_AND_HUNTER, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hunter(-300.0F, 0.0F, false),
                        new EssenceThief(100.0F, 150.0F)
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
                        new BlueCrystal(-350.0F, 400.0F),
                        new GoldenCrystal(250.0F, 400.0F),
                        new GreenCrystal(-450.0F, 200.0F),
                        new RedCrystal(350.0F, 200.0F),
                        new PurpleCrystal(-350.0F, 0.0F),
                        new GrayCrystal(250.0F, 0.0F),
                        new TheCrystal(-50.0F, 50.0F)
                }));
        BaseMod.addBoss(AbyssAct.ID, Encounters.CRYSTALS, "abyss/images/map/bosses/Crystals.png", "abyss/images/map/bosses/CrystalsOutline.png");

        //Special fights
        BaseMod.addMonster(Encounters.SQUIRMING_HORRORS_2, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new SquirmingHorror(-250.0F, 0.0F),
                        new SquirmingHorror(150.0F, 0.0F)
                }));

        //Act 4 enemies
        BaseMod.addMonster(Encounters.ANNIHILATION_DUO, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new AnnihilationWarrior(-250.0F, 0.0F),
                        new AnnihilationMage(150.0F, 0.0F)
                }));
        BaseMod.addMonster(UniversalVoid.ID, (BaseMod.GetMonster)UniversalVoid::new);
        BaseMod.addBoss(VoidAct.ID, UniversalVoid.ID, "abyss/images/map/bosses/UniversalVoid.png", "abyss/images/map/bosses/UniversalVoidOutline.png");
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
        BaseMod.addEvent(DeepShrine.ID, DeepShrine.class, AbyssAct.ID);
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