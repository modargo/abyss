package abyss.util;

import com.megacrit.cardcrawl.random.Random;

import java.util.Collections;
import java.util.List;

public class CollectionsUtil {
    public static void shuffle(List<?> list, Random rng) {
        Collections.shuffle(list, new java.util.Random(rng.randomLong()));
    }
}
