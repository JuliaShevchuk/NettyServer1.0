package command;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import status.StatisticCounter;
import java.net.InetSocketAddress;
import java.util.ResourceBundle;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * Created by Julia on 26.05.2015.
 */
public abstract class Command {


    public static final String REDIRECT = "REDIRECT";
    private static final String DEFAULT = "DEFAULT";
    private static final String FILENAME = "config.cf";

    private ResourceBundle resource = ResourceBundle.getBundle(FILENAME);

    public abstract void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector);

    public void checkStatus(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String uri = req.getUri();
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString();

            if (!uri.equals(resource.getObject(DEFAULT))) {

                statisticCollector.updateUrlMap(uri);

                if (!uri.contains(resource.getObject(REDIRECT).toString())) {
                    statisticCollector.setQuantityRequest(statisticCollector.getQuantityRequest() + 1);
                    statisticCollector.updateStatusList(ip, uri);
                    statisticCollector.updateIpMap(ip);
                    statisticCollector.updateUniqueIpSet(ip);

                }
            }
        }
    }

    public void sendRequest(ChannelHandlerContext ctx, Object msg, FullHttpResponse response, boolean timeout) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            boolean keepAlive = HttpHeaders.isKeepAlive(req);

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);

                if (timeout) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ctx.write(response);
            }

        }

    }

}