package com.moneytransfer.atreyee;

import com.moneytransfer.atreyee.api.BankingAPI;
import com.moneytransfer.atreyee.util.CommonUtil;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;

public class MoneyTransferApplication {
    public static void main(String[] args) throws IOException {
        ResourceConfig packageResourceConfig = new ResourceConfig(BankingAPI.class);

        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(CommonUtil.getURI(), packageResourceConfig);

        httpServer.start();
        System.out.println("Server started");
    }
}
