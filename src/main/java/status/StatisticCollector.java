package status;

import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.*;

/**
 * Created by yuliya.shevchuk on 05.08.2015.
 */
public class StatisticCollector extends ChannelTrafficShapingHandler {

    private int quantityRequest;
    private Map<String, Integer> urlMap;
    private Map<String, IpCounter> ipMap;

    private List<Request> statusList;
    private int activeRequest;

    private TrafficCounter trafficCounter;


    public StatisticCollector() {
        super(100);
        urlMap = new HashMap<String, Integer>();
        ipMap = new HashMap<String, IpCounter>();
        statusList = new ArrayList<>(16);
        trafficCounter = trafficCounter();
    }

    public synchronized void updateIpMap(String ip) {
        IpCounter ipCounter = new IpCounter();
        if (ipMap.get(ip) == null) {
            ipCounter.setQuantity(1);
        } else {
            ipCounter = ipMap.get(ip);
            ipCounter.setQuantity(ipCounter.getQuantity() + 1);
        }
        ipCounter.setDate((new Date()));
        ipMap.put(ip, ipCounter);
    }

    public synchronized void updateUrlMap(String url) {

        if (urlMap.get(url) == null) {
            urlMap.put(url, new Integer(1));
        } else {
            urlMap.put(url, urlMap.get(url) + 1);
        }

    }

    public synchronized int getUniqueIp() {
        Set keys = urlMap.keySet();
        return keys.size();
    }

    public synchronized void updateStatusList(String ip, String url) {

        if (statusList.size() == 16) {
            statusList.remove(1);
        }
        Request status = new Request();
        status.setIp(ip);
        status.setUrl(url);
        status.setTimestamp((new Date()));

        status.setSentBytes(trafficCounter.cumulativeWrittenBytes());
        status.setReceivedBytes(trafficCounter.cumulativeReadBytes());
        status.setSpeed((trafficCounter.lastWriteThroughput() + trafficCounter.lastReadThroughput()) / 2);
        statusList.add(status);

    }

    public synchronized int getQuantityRequest() {
        return quantityRequest;
    }

    public synchronized void setQuantityRequest(int quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public synchronized Map<String, Integer> getUrlMap() {
        return urlMap;
    }

    public synchronized void setUrlMap(Map<String, Integer> urlMap) {
        this.urlMap = urlMap;
    }

    public synchronized Map<String, IpCounter> getIpMap() {
        return ipMap;
    }

    public synchronized void setIpMap(Map<String, IpCounter> ipMap) {
        this.ipMap = ipMap;
    }

    public synchronized int getActiveRequest() {
        return activeRequest;
    }

    public synchronized void setActiveRequest(int activeRequest) {
        this.activeRequest = activeRequest;
    }

    public synchronized List<Request> getStatusList() {
        return statusList;
    }

    public synchronized void setStatusList(List<Request> statusList) {
        this.statusList = statusList;
    }

}
