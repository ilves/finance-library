package ee.golive.finance.helper;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

public class DateTimeHelper {

    public static Period MONTHLY = Period.months(1);

    public static List<Interval> getIntervals(DateTime start, DateTime end, Period period) {
        List<Interval> intervals = new ArrayList<>();
        Interval interval;
        do {
            interval = new Interval(start, start.plus(period));
            intervals.add(interval);
            start = interval.getEnd();
        } while (interval.getEnd().compareTo(end) < 0);
        return intervals;
    }
}
