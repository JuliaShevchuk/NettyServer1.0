package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import status.StatisticCounter;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private StatisticCounter statisticCounter;
    public static final String TRAFFIC_COUNTER = "trafficCounter";
    public static final String SERVER_CODEC = "serverCodec";
    public static final String SERVER_HANDLER = "serverHandler";

    public ServerInitializer() {
        statisticCounter = new StatisticCounter();
    }


    @Override
    public void initChannel(SocketChannel ch) {

        ChannelPipeline p = ch.pipeline();

        p.addLast(TRAFFIC_COUNTER, new ChannelTrafficShapingHandler(
                AbstractTrafficShapingHandler.DEFAULT_CHECK_INTERVAL));
        p.addLast(SERVER_CODEC, new HttpServerCodec());
        p.addLast(SERVER_HANDLER, new ServerHandler(statisticCounter));

    }
}