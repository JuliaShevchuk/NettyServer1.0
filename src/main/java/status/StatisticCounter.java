package status;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.*;

/**
 * Created by yuliya.shevchuk on 05.08.2015.
 */

@ChannelHandler.Sharable
public class StatisticCounter {

    public static final String FILENAME = "config.cf";
    public static final String REDIRECT = "REDIRECT";
    private int quantityRequest;
    private Map<String, Integer> urlMap;
    private Map<String, IpCounter> ipMap;
    private final ResourceBundle resource;
    private List<Request> statusList;
    private Set<String> uniqueIpSet;
    private long activeConnections;


    public StatisticCounter() {

        urlMap = new TreeMap<String, Integer>();
        ipMap = new HashMap<String, IpCounter>();
        uniqueIpSet = new HashSet<>();
        statusList = new ArrayList<>(16);
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

        if (statusList.size() == 16) {
            statusList.remove(1);
        }
        Request status = new Request();
        status.setIp(ip);
        status.setUrl(url);
        status.setTimestamp((new Date()));

        status.setSentBytes(counter.cumulativeWrittenBytes());
        status.setReceivedBytes(counter.cumulativeReadBytes());
        status.setSpeed((counter.lastReadThroughput()));
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

    public synchronized List<Request> getStatusList() {
        return statusList;
    }

    public synchronized void setStatusList(List<Request> statusList) {
        this.statusList = statusList;
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
}