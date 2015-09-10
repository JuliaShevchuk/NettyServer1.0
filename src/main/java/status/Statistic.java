package status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by yuliya.shevchuk on 07.08.2015.
 */
public class Statistic {

    private String ip;
    private String uri;
    private AtomicReference<Date> timestamp;
    private AtomicLong sentBytes;
    private AtomicLong receivedBytes;
    private AtomicLong speed;


    public Statistic() {
        timestamp = new AtomicReference<>();
        sentBytes = new AtomicLong();
        receivedBytes = new AtomicLong();
        speed = new AtomicLong();

    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return uri;
    }

    public void setUrl(String uri) {

        this.uri = uri;
    }

    public AtomicReference<Date> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(AtomicReference<Date> timestamp) {
        this.timestamp = timestamp;
    }

    public AtomicLong getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(AtomicLong sentBytes) {
        this.sentBytes = sentBytes;
    }

    public AtomicLong getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(AtomicLong receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public AtomicLong getSpeed() {
        return speed;
    }

    public void setSpeed(AtomicLong speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistic)) return false;

        Statistic request = (Statistic) o;

        return receivedBytes.equals(request.receivedBytes)
                && sentBytes.equals(request.sentBytes)
                && speed.equals(request.speed)
                && ip.equals(request.ip)
                && timestamp.equals(request.timestamp)
                && uri.equals(request.uri);

    }

    @Override
    public int hashCode() {
        return 17 * ip.hashCode()
                + 31 * uri.hashCode()
                + 63 * timestamp.hashCode()
                + (int) (sentBytes.get() + receivedBytes.get() + speed.get());
    }

}