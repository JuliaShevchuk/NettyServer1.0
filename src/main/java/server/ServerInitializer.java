package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import status.StatisticCounter;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private SslContext sslCtx;
    private StatisticCounter statisticCounter;
    public static final String TRAFFIC_COUNTER = "trafficCounter";
    public static final String SERVER_CODEC = "serverCodec";
    public static final String SERVER_HANDLER = "serverHandler";

    public ServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
        statisticCounter = new StatisticCounter();
    }


    @Override
    public void initChannel(SocketChannel ch) {

        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }


        p.addLast(TRAFFIC_COUNTER, new ChannelTrafficShapingHandler(
                AbstractTrafficShapingHandler.DEFAULT_CHECK_INTERVAL));
        p.addLast(SERVER_CODEC, new HttpServerCodec());
        p.addLast(SERVER_HANDLER, new ServerHandler(statisticCounter));

    }
}