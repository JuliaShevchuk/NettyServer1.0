package status;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuliya.shevchuk on 07.08.2015.
 */
public class Request {

    private String ip;
    private String url;
    private Date timestamp;
    private long sentBytes;
    private long receivedBytes;
    private long speed;



    public synchronized String getIp() {
        return ip;
    }

    public synchronized void setIp(String ip) {
        this.ip = ip;
    }

    public synchronized String getUrl() {
        return url;
    }

    public synchronized void setUrl(String url) {
        this.url = url;
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
    public synchronized String toString() {
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        return ip + "  " +
                url + "  " +
                simpleDateFormat.format(timestamp) + "  " +
                sentBytes + "   " +
                receivedBytes + "  " +
                speed;

    }
}
