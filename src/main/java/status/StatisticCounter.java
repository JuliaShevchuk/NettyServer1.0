package status;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by yuliya.shevchuk on 05.08.2015.
 */


public class StatisticCounter {

    public static final String FILENAME = "config.cf";
    public static final String REDIRECT = "REDIRECT";
    private int quantityRequest;
    private ConcurrentMap<String, Integer> urlMap;
    private ConcurrentMap<String, IpCounter> ipMap;
    private final ResourceBundle resource;
    private BlockingQueue<Request> statusQueue;
    private Set<String> uniqueIpSet;
    private long activeConnections;


    public StatisticCounter() {

        urlMap = new ConcurrentSkipListMap<String, Integer>();
        ipMap = new ConcurrentHashMap<String, IpCounter>();
        uniqueIpSet = new ConcurrentSkipListSet<>();
        statusQueue = new ArrayBlockingQueue<>(16);
        resource = ResourceBundle.getBundle(FILENAME);

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

    public synchronized void updateUrlMap(String uri) {

        if (uri.contains(resource.getObject(REDIRECT).toString())) {
            String url = uri.substring(14);
            if (!urlMap.containsKey(url)) {
                urlMap.put(url, 1);
            } else {
                urlMap.put(url, urlMap.get(url) + 1);
            }
        }

    }

    public synchronized void updateUniqueIpSet(String ip) {
        uniqueIpSet.add(ip);
    }

    public synchronized void updateStatusList(ChannelHandlerContext ctx, String ip, String url) {
        ChannelHandler trafficHandler = ctx.pipeline().get("trafficCounter");
        ChannelTrafficShapingHandler channelTrafficShapingHandler = (ChannelTrafficShapingHandler) trafficHandler;
        TrafficCounter counter = channelTrafficShapingHandler.trafficCounter();
        if (statusQueue.size() == 16) {
            statusQueue.remove();
        }
        Request status = new Request();
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

    public BlockingQueue<Request> getStatusQueue() {
        return statusQueue;
    }

    public void setStatusQueue(BlockingQueue<Request> statusQueue) {
        this.statusQueue = statusQueue;
    }
    public ConcurrentMap<String, Integer> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(ConcurrentMap<String, Integer> urlMap) {
        this.urlMap = urlMap;
    }

    public ConcurrentMap<String, IpCounter> getIpMap() {
        return ipMap;
    }

    public void setIpMap(ConcurrentMap<String, IpCounter> ipMap) {
        this.ipMap = ipMap;
    }


}