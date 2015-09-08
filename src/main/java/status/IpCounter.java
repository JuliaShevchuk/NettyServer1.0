package status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by yuliya.shevchuk on 07.08.2015.
 */
public class IpCounter {

    private AtomicLong quantity;
    private AtomicReference<Date> date;
    private String ip;

    public IpCounter() {

        quantity = new AtomicLong();
        date = new AtomicReference<>();

    }

    public IpCounter(String ip, AtomicLong quantity, AtomicReference<Date> date) {
        this();
        this.ip = ip;
        this.quantity = quantity;
        this.date = date;
    }

    public AtomicLong getQuantity() {
        return quantity;
    }

    public void setQuantity(AtomicLong quantity) {
        this.quantity = quantity;
    }

    public AtomicReference<Date> getDate() {
        return date;
    }

    public void setDate(AtomicReference<Date> date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpCounter)) return false;

        IpCounter ipCounter = (IpCounter) o;

        return quantity == ipCounter.quantity && date.equals(ipCounter.date);
    }

    @Override
    public int hashCode() {
        return 31 * (int) quantity.get() + date.hashCode();

    }


}