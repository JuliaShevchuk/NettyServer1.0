package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import status.StatisticCollector;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private SslContext sslCtx;
    private StatisticCollector statisticCollector;

    public ServerInitializer(SslContext sslCtx, StatisticCollector statisticCollector) {
        this.sslCtx = sslCtx;
        this.statisticCollector = statisticCollector;
    }


    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpServerCodec());
        p.addLast(new ServerHandler());
        p.addLast(new ChannelInboundHandlerAdapter() {


            @Override
            public void channelActive(final ChannelHandlerContext ctx) {
                statisticCollector.setActiveRequest(statisticCollector.getActiveRequest() + 1);
            }

            @Override
            public void channelInactive(final ChannelHandlerContext ctx) {
                statisticCollector.setActiveRequest(statisticCollector.getActiveRequest() - 1);
            }

        });

    }
}
