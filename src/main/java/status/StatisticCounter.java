package status;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by yuliya.shevchuk on 05.08.2015.
 */


public class StatisticCounter {


    private AtomicLong quantityRequest;
    private AtomicLong activeConnections;

    private List<IpCounter> ipList;
    private ConcurrentMap<String, Integer> urlMap;
    private Queue<Statistic> statusQueue;
    private Set<String> uniqueIpSet;

    public static final String TRAFFIC_COUNTER = "trafficCounter";


    public StatisticCounter() {

        quantityRequest = new AtomicLong();
        activeConnections = new AtomicLong();

        urlMap = new ConcurrentSkipListMap();
        ipList = new CopyOnWriteArrayList();
        uniqueIpSet = new ConcurrentSkipListSet();
        statusQueue = new ConcurrentLinkedQueue();

    }

    public synchronized void updateIpList(String ip) {

        if (ipList.size() != 0) {

            for (IpCounter ipCounter : ipList) {
                if (ipCounter.getIp().equals(ip)) {
                    ipCounter.setDate(new AtomicReference<>(new Date()));
                    ipCounter.setQuantity(new AtomicLong(ipCounter.getQuantity().incrementAndGet()));
                    return;
                }
            }
        }
        ipList.add(new IpCounter(ip, new AtomicLong(1), new AtomicReference<>(new Date())));
    }

    public synchronized void updateUrlMap(String uri) {

        if (!urlMap.containsKey(uri)) {
            urlMap.put(uri, 1);
        } else {
            urlMap.put(uri, urlMap.get(uri) + 1);
        }

    }

    public void updateUniqueIpSet(String ip) {
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
        status.setTimestamp(new AtomicReference<>(new Date()));

        status.setSentBytes(new AtomicLong(counter.cumulativeWrittenBytes()));
        status.setReceivedBytes(new AtomicLong(counter.cumulativeReadBytes()));
        status.setSpeed(new AtomicLong(counter.lastReadThroughput()));

        statusQueue.add(status);

    }


    public AtomicLong getQuantityRequest() {
        return quantityRequest;
    }

    public void setQuantityRequest(AtomicLong quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public Set<String> getUniqueIpSet() {
        return uniqueIpSet;
    }

    public void setUniqueIpSet(Set<String> uniqueIpSet) {
        this.uniqueIpSet = uniqueIpSet;
    }

    public AtomicLong getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(AtomicLong activeConnections) {
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

    public Queue<Statistic> getStatusQueue() {
        return statusQueue;
    }

    public void setStatusQueue(BlockingQueue<Statistic> statusQueue) {
        this.statusQueue = statusQueue;
    }
}