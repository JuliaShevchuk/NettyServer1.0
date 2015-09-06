package status;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.*;
import java.util.concurrent.*;


/**
 * Created by yuliya.shevchuk on 05.08.2015.
 */


public class StatisticCounter {


    private int quantityRequest;
    private long activeConnections;

    private List<IpCounter> ipList;
    private ConcurrentMap<String, Integer> urlMap;
    private BlockingQueue<Statistic> statusQueue;
    private Set<String> uniqueIpSet;


    public static final String TRAFFIC_COUNTER = "trafficCounter";


    public StatisticCounter() {

        urlMap = new ConcurrentSkipListMap();
        ipList = new CopyOnWriteArrayList();
        uniqueIpSet = new ConcurrentSkipListSet<>();
        statusQueue = new ArrayBlockingQueue<>(16);


    }

    public synchronized void updateIpList(String ip) {

        if (ipList.size() != 0) {

            for (IpCounter ipCounter : ipList) {
                if (ipCounter.getIp().equals(ip)) {
                    ipCounter.setDate(new Date());
                    ipCounter.setQuantity(ipCounter.getQuantity() + 1);
                    return;
                }
            }
        }
        ipList.add(new IpCounter(ip, 1, new Date()));
    }

    public synchronized void updateUrlMap(String uri) {

        if (!urlMap.containsKey(uri)) {
            urlMap.put(uri, 1);
        } else {
            urlMap.put(uri, urlMap.get(uri) + 1);
        }

    }

    public synchronized void updateUniqueIpSet(String ip) {
        uniqueIpSet.add(ip);

    }

    public synchronized void updateStatusList(ChannelHandlerContext ctx, String ip, String url) {

        TrafficCounter counter = ((ChannelTrafficShapingHandler) ctx.pipeline().get(TRAFFIC_COUNTER)).trafficCounter();

        if (statusQueue.size() == 16) {
            statusQueue.remove();
        }

        Statistic status = new Statistic();
        status.setIp(ip);
        status.setUrl(url);
        status.setTimestamp((new Date()));

        status.setSentBytes(counter.cumulativeWrittenBytes());
        status.setReceivedBytes(counter.cumulativeReadBytes());
        status.setSpeed((counter.lastReadThroughput()));

        statusQueue.add(status);

    }


    public synchronized int getQuantityRequest() {
        return quantityRequest;
    }

    public synchronized void setQuantityRequest(int quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public synchronized Set<String> getUniqueIpSet() {
        return uniqueIpSet;
    }

    public synchronized void setUniqueIpSet(Set<String> uniqueIpSet) {
        this.uniqueIpSet = uniqueIpSet;
    }

    public synchronized long getActiveConnections() {
        return activeConnections;
    }

    public synchronized void setActiveConnections(long activeConnections) {
        this.activeConnections = activeConnections;
    }

    public ConcurrentMap<String, Integer> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(ConcurrentMap<String, Integer> urlMap) {
        this.urlMap = urlMap;
    }

    public List<IpCounter> getIpList() {
        return ipList;
    }

    public void setIpList(List<IpCounter> ipList) {
        this.ipList = ipList;
    }

    public BlockingQueue<Statistic> getStatusQueue() {
        return statusQueue;
    }

    public void setStatusQueue(BlockingQueue<Statistic> statusQueue) {
        this.statusQueue = statusQueue;
    }
}