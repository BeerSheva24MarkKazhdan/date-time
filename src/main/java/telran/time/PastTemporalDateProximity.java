package telran.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;

public class PastTemporalDateProximity implements TemporalAdjuster{
    Temporal[] array;
public PastTemporalDateProximity(Temporal [] array){
        this.array = Arrays.copyOf(array, array.length);
        Arrays.sort(this.array);
}

    @Override
    public Temporal adjustInto(Temporal temporal) {
        if (!temporal.isSupported(ChronoUnit.DAYS)){
throw new IllegalArgumentException("Temporal must support DAY_OF_MONTH, MONTH_OF_YEAR, and YEAR");
        }
        Temporal pastDate = getPreviousIndex(temporal);
        if (pastDate == null) {
            throw new IllegalStateException("No properly past date has found");
        }
        return pastDate;
    }

    private Temporal getPreviousIndex(Temporal temporal) {
        int start = 0, end = this.array.length - 1;
        Temporal pastDate = null;

        while(start <= end) {
            int mid = start + (end - start) / 2;
            Temporal midDate = this.array[mid];
            long daysDiff = temporal.until(midDate, ChronoUnit.DAYS);
            if (daysDiff < 0){
                pastDate = midDate;
                start = mid +1;
            } else {
                end = mid - 1;
            }
    } return pastDate;
  }
}

