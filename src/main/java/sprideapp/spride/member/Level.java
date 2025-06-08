package sprideapp.spride.member;

public enum Level {
    BRONZE(0, 99, "SILVER"),
    SILVER(100, 299, "GOLD"),
    GOLD(300, 599, "PLATINUM"),
    PLATINUM(600, 999, "DIAMOND"),
    DIAMOND(1000, 1499, "MASTER"),
    MASTER(1500, Integer.MAX_VALUE, null); // 마지막은 다음 티어 없음

    private final int minScore;
    private final int maxScore;
    private final String next;

    Level(int minScore, int maxScore, String next) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.next = next;
    }

    public Level getNextTier() {
        return next == null ? null : Level.valueOf(next);
    }

    public static Level fromScore(int score) {
        for (Level tier : Level.values()) {
            if (score >= tier.minScore && score <= tier.maxScore) {
                return tier;
            }
        }
        return null;
    }
}
