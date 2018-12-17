package Model;

import java.time.*;
import java.util.TimeZone;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private static DateConverter dateConverter = null;

    public static DateConverter getInstance() {
        return dateConverter == null ? (dateConverter = new DateConverter()) : dateConverter;
    }

    public long dateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    public long dateTimeTotimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public LocalDateTime timestampToDateTime(long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp),
                TimeZone.getDefault().toZoneId()
        );
    }
    public String localDateToString(long timeStamp)
    {
        LocalDateTime dateTime = timestampToDateTime(timeStamp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

        String formatDateTime = dateTime.format(formatter);
        return formatDateTime;
    }

    public String minutesToHourMinutes(int time)
    {
        System.out.println(time);
        int hours = (int)time /3600;
        int remainder = (int)time - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        String ret = String.valueOf(hours) + ":" + String.valueOf(minutes) +":" + String.valueOf(seconds);
        return ret;
    }
}
