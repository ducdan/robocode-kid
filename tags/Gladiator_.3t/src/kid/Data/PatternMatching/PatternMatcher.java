package kid.Data.PatternMatching;

public class PatternMatcher {

    Pattern[] RobotMovment;
    Pattern[] RecentMovment;
    int RecentMovmentLength;

    public PatternMatcher(Pattern[] RobotMovment) {
        this.RobotMovment = RobotMovment;
    }

    public Pattern[] MatchPattern(int arrayPosition, int RecentMovmentLength) {
        if (arrayPosition > RecentMovmentLength) {
            arrayPosition -= RecentMovmentLength;
            this.RecentMovmentLength = RecentMovmentLength;
            RecentMovment = new Pattern[RecentMovmentLength];
            for (int i = 0; i < RecentMovmentLength; i++, arrayPosition++)
                RecentMovment[i] = RobotMovment[arrayPosition];

            int start = 0;
            double dif = Double.POSITIVE_INFINITY;
            for (int i = (arrayPosition - (RecentMovmentLength * 2)); i > -1; i--) {
                double d = getDifference(i);
                if (dif >= d) {
                    dif = d;
                    start = i;
                }
            }

            Pattern[] pattern = new Pattern[arrayPosition - start];
            int p = 0, rm = (start + RecentMovmentLength);
            for (; p < pattern.length; p++, rm++) {
                pattern[p] = RobotMovment[rm];
            }

            return pattern;
        }
        return null;
    }

    private double getDifference(int StartAt) {
        double dif = 0;
        int j = 0; int i = StartAt;
        for (; j < RecentMovmentLength; i++, j++)
            dif += RobotMovment[i].difference(RecentMovment[j]);
        return dif;
    }

}
