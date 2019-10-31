package com.moneytransfer.atreyee.api;

import com.moneytransfer.atreyee.model.FundTransferRequestModel;
import com.moneytransfer.atreyee.model.FundTransferResponseModel;
import com.moneytransfer.atreyee.service.FundTransferService;
import com.moneytransfer.atreyee.service.FundTransferServiceImpl;
import org.apache.log4j.Logger;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.sql.SQLException;

@Path("/api")
public class BankingAPI {
    static final Logger log = Logger.getLogger(BankingAPI.class);

    private static FundTransferService fundTransferService;

    private FundTransferService getServiceInstance() throws SQLException {
        if (fundTransferService == null) {
            log.debug("initializing the fundtransferservice and db connection ");

            fundTransferService = new FundTransferServiceImpl();

            log.debug("initializing the database and inserting dummy data");
            fundTransferService.initializeDB();
        }
        return fundTransferService;
    }

    @POST
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public FundTransferResponseModel transfer(FundTransferRequestModel userInput) throws SQLException {
        log.debug("fund transfer request initiated");
        FundTransferResponseModel response;
        response = getServiceInstance().transferMoney(userInput);
        log.debug("response received" + response);
        return response;
    }


}