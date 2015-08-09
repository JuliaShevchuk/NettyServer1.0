package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import status.StatisticCounter;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private SslContext sslCtx;
    private StatisticCounter statisticCollector;

    public ServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
        statisticCollector = new StatisticCounter();
    }


    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }


        p.addLast("traffic-counter", statisticCollector);
        p.addLast("serverCodec", new HttpServerCodec());
        p.addLast("serverHandler",new ServerHandler(statisticCollector));


    }
}
