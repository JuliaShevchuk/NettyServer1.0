package server;

import command.Command;
import command.CommandHelper;
import config.Config;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import status.StatisticCollector;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {


    private Command command;
    private StatisticCollector statisticCollector;

    public ServerHandler() {
        statisticCollector = new StatisticCollector();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            command = CommandHelper.getInstance().getCommand(Config.getInstance().getProperty(req.getUri()));
            command.execute(ctx, req, statisticCollector);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
