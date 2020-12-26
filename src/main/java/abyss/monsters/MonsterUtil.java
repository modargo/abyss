package abyss.monsters;

import abyss.powers.crystals.PurpleCrystalPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class MonsterUtil {
    public static boolean lastMoveX(AbstractMonster m, byte move, int movesAgo) {
        return m.moveHistory.size() >= movesAgo && m.moveHistory.get(m.moveHistory.size() - movesAgo) == move;
    }

    public static List<AbstractPower> getMonsterPowers(String powerId) {
        List<AbstractPower> powers = new ArrayList<>();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying) {
                AbstractPower power = m.getPower(powerId);
                if (power != null) {
                    powers.add(power);
                }
            }
        }
        return powers;
    }
}
