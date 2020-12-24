package abyss.events;

import abyss.act.Encounters;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

public class AbyssalSphere extends AbstractEvent {
    public static final String ID = "Abyss:AbyssalSphere";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String INTRO_MSG;
    private AbyssalSphere.CurScreen screen;

    public AbyssalSphere() {
        this.screen = AbyssalSphere.CurScreen.INTRO;
        this.initializeImage("images/events/sphereClosed.png", 1120.0F * Settings.xScale, AbstractDungeon.floorY - 50.0F * Settings.scale);
        this.body = INTRO_MSG;
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(OPTIONS[1]);
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Encounters.SQUIRMING_HORRORS);
    }

    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }

    }

    protected void buttonEffect(int buttonPressed) {
        switch(this.screen) {
            case INTRO:
                switch(buttonPressed) {
                    case 0:
                        this.screen = AbyssalSphere.CurScreen.PRE_COMBAT;
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[2]);
                        this.roomEventText.clearRemainingOptions();
                        logMetric(ID, "Fight");
                        return;
                    case 1:
                        this.screen = AbyssalSphere.CurScreen.END;
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                        this.roomEventText.clearRemainingOptions();
                        logMetricIgnored(ID);
                        return;
                    default:
                        return;
                }
            case PRE_COMBAT:
                if (Settings.isDailyRun) {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(50));
                } else {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(45, 55));
                }

                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.RARE));
                if (this.img != null) {
                    this.img.dispose();
                    this.img = null;
                }

                this.img = ImageMaster.loadImage("images/events/sphereOpen.png");
                this.enterCombat();
                AbstractDungeon.lastCombatMetricKey = "2 Orb Walkers";
                break;
            case END:
                this.openMap();
        }

    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        INTRO_MSG = DESCRIPTIONS[0];
    }

    private enum CurScreen {
        INTRO,
        PRE_COMBAT,
        END
    }
}
