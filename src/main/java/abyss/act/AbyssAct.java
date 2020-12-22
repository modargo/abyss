package abyss.act;

import abyss.monsters.elites.Behemoth;
import abyss.monsters.elites.GnawingCorruption;
import abyss.monsters.elites.PrimevalQueen;
import abyss.monsters.normals.BoundAbyssal;
import abyss.monsters.normals.GluttonousAbomination;
import abyss.monsters.normals.SquirmingHorror;
import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.scenes.TheBeyondScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class AbyssAct extends CustomDungeon {
    public static final String ID = "Abyss:Abyss";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public static final int ACT_NUM = 3;
    private static final Logger logger = LogManager.getLogger(AbyssAct.class.getName());

    public AbyssAct() {
        super(NAME, ID, "images/ui/event/panel.png", false, 2, 12, 10);
        logger.info("AbyssAct constructor");
    }

    public AbyssAct(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }
    public AbyssAct(CustomDungeon cd, AbstractPlayer p, SaveFile sf) {
        super(cd, p, sf);
    }

    @Override
    public AbstractScene DungeonScene() {
        logger.info("AbyssAct DungeonScene");
        return new TheBeyondScene();
    }

    @Override
    public String getBodyText() {
        return TEXT[2];
    }

    @Override
    public String getOptionText() {
        return TEXT[3];
    }

    @Override
    protected void initializeLevelSpecificChances() {
        //These are all deliberately the same as Beyond
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    @Override
    protected void generateMonsters() {
        generateWeakEnemies(2);
        generateStrongEnemies(strongpreset);
        generateElites(elitepreset);
    }

    @Override
    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(SquirmingHorror.ID, 1.0F));
        monsters.add(new MonsterInfo(Encounters.VICIOUS_HATCHLINGS, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Encounters.VICIOUS_HATCHLINGS_HARD, 1.0F));
        monsters.add(new MonsterInfo(GluttonousAbomination.ID, 1.0F));
        monsters.add(new MonsterInfo(BoundAbyssal.ID, 1.0F));
        monsters.add(new MonsterInfo(Encounters.CROAKING_TRIO, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Behemoth.ID, 1.0F));
        monsters.add(new MonsterInfo(GnawingCorruption.ID, 1.0F));
        monsters.add(new MonsterInfo(PrimevalQueen.ID, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    @Override
    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList<>();
        switch (monsterList.get(monsterList.size() - 1))
        {
            case Encounters.VICIOUS_HATCHLINGS:
                retVal.add(Encounters.VICIOUS_HATCHLINGS_HARD);
                break;
        }

        return retVal;
    }

    @Override
    protected void initializeShrineList() {
        // No shrines in this act, since we want to experience as many of the new events as possible
    }

    @Override
    protected void initializeEventList() {
        // Events are added via BaseMod in Abyss.addEvents()
    }
}
