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
        return new Formatter().format("%-15d%-25s%n", quantity,
                new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date)).toString();
    }

}