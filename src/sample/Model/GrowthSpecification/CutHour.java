package sample.Model.GrowthSpecification;

import java.io.Serializable;

/**
 * Created by Jose on 06/10/2014.
 */
public class CutHour implements Serializable {
    String hour;
    String minute;

    public CutHour() {
    }

    public CutHour(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "CutHour{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}
