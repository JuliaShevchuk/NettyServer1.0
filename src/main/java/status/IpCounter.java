package status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by yuliya.shevchuk on 07.08.2015.
 */
public class IpCounter {

    private long quantity;
    private Date date;
    private String ip;

    public IpCounter() {
    }

    public IpCounter(String ip, long quantity, Date date) {
        this.ip = ip;
        this.quantity = quantity;
        this.date = date;
    }

    public synchronized long getQuantity() {
        return quantity;
    }

    public synchronized void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public synchronized Date getDate() {
        return date;
    }

    public synchronized void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpCounter)) return false;

        IpCounter ipCounter = (IpCounter) o;

        return quantity == ipCounter.quantity && date.equals(ipCounter.date);
    }

    @Override
    public synchronized int hashCode() {
        return 31 * (int) quantity + date.hashCode();

    }

    @Override
    public synchronized String toString() {
        return new Formatter().format("%-18s%-15s%-25s%n", ip, quantity,
                new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date)).toString();
    }

}