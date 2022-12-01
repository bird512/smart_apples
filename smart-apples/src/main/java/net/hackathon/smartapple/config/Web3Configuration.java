package net.hackathon.smartapple.config;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.hackathon.smartapple.SmartAppleApp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

@Configuration
//@Slf4j
public class Web3Configuration {

    private static final Logger log = LoggerFactory.getLogger(Web3Configuration.class);

    @Value("${blockchain.rpc}")
    String defaultRpc;

    @Bean
    public Web3j web3j() {
        return getWeb3j(defaultRpc);
    }

    public Web3j getWeb3j(String rpc) {
        log.debug("-----------------init web3j: " + rpc);
        Web3j web3j = null;
        try {
            if (rpc.startsWith("http")) {
                web3j = Web3j.build(new HttpService(rpc, createOkHttpClient(), false));
            } else if (rpc.startsWith("ws")) {
                final WebSocketClient webSocketClient = new WebSocketClient(new URI(rpc));
                final boolean includeRawResponses = true;
                final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);
                webSocketService.connect();
                web3j = Web3j.build(webSocketService);
            } else {
                web3j = Web3j.build(new UnixIpcService(rpc));
            }
        } catch (Exception e) {
            log.warn("failed to build the web3j", e.getMessage());
        }
        return web3j;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = 300L;
        if (tos != null) {
            builder.connectTimeout(tos, TimeUnit.SECONDS);
            builder.readTimeout(tos, TimeUnit.SECONDS); // Sets the socket timeout too
            builder.writeTimeout(tos, TimeUnit.SECONDS);
        }
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }
}
