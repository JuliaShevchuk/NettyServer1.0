package status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;


/**
 * Created by yuliya.shevchuk on 07.08.2015.
 */
public class Statistic {

    private String ip;
    private String uri;
    private Date timestamp;
    private long sentBytes;
    private long receivedBytes;
    private long speed;


    public Statistic() {
    }


    public synchronized String getIp() {
        return ip;
    }

    public synchronized void setIp(String ip) {
        this.ip = ip;
    }

    public synchronized String getUrl() {
        return uri;
    }

    public synchronized void setUrl(String uri) {

        this.uri = uri;
    }

    public synchronized Date getTimestamp() {
        return timestamp;
    }

    public synchronized void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public synchronized long getSentBytes() {
        return sentBytes;
    }

    public synchronized void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public synchronized long getReceivedBytes() {
        return receivedBytes;
    }

    public synchronized void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public synchronized long getSpeed() {
        return speed;
    }

    public synchronized void setSpeed(long speed) {
        this.speed = speed;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistic)) return false;

        Statistic request = (Statistic) o;

        return receivedBytes == request.receivedBytes
                && sentBytes == request.sentBytes
                && speed == request.speed
                && ip.equals(request.ip)
                && timestamp.equals(request.timestamp)
                && uri.equals(request.uri);

    }

    @Override
    public synchronized int hashCode() {
        return 17 * ip.hashCode()
                + 31 * uri.hashCode()
                + 63 * timestamp.hashCode()
                + (int) (sentBytes + receivedBytes + speed);
    }

    @Override
    public synchronized String toString() {

        return new Formatter().format("%-18s%-40s%-25s%-20d%-20d%-20d%n", ip, uri,
                new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(timestamp),
                sentBytes, receivedBytes, speed).toString();

    }
}