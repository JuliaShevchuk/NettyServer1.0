package status;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return  quantity + "  " + simpleDateFormat.format(date) ;
    }
}
