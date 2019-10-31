package com.atreye.moneytransfer.test;

import com.moneytransfer.atreyee.api.BankingAPI;
import com.moneytransfer.atreyee.exception.TransactionMessages;
import com.moneytransfer.atreyee.model.FundTransferRequestModel;
import com.moneytransfer.atreyee.model.FundTransferResponseModel;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class MoneyTransferAppTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(BankingAPI.class);
    }

    @Test
    public void testTransferWithAllValidData() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(100.0);
        request.setFromAccount("GB53 REVO 0099 7052 8969 08");
        request.setToAccount("LT22 3250 0403 2084 3273");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(200, responseEntity.getResponseCode());
    }

    @Test
    public void testResponseDataType() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(100.0);
        request.setFromAccount("GB53 REVO 0099 7052 8969 08");
        request.setToAccount("LT22 3250 0403 2084 3273");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertNotNull(responseEntity);
    }

    @Test
    public void testTransferWithInvalidSenderData() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(100.0);
        request.setFromAccount("");
        request.setToAccount("LT22 3250 0403 2084 3273");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(400, responseEntity.getResponseCode());
    }

    @Test
    public void testTransferWithInvalidReceiverData() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(100.0);
        request.setFromAccount("LT22 3250 0403 2084 3273");
        request.setToAccount("");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(400, responseEntity.getResponseCode());
    }

    @Test
    public void testTransferWithInsufficientAmount() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(10000.0);
        request.setFromAccount("GB53 REVO 0099 7052 8969 08");
        request.setToAccount("LT22 3250 0403 2084 3273");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(400, responseEntity.getResponseCode());
    }

    @Test
    public void testInsufficientAmountErrorMessage() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(10000.0);
        request.setFromAccount("GB53 REVO 0099 7052 8969 08");
        request.setToAccount("LT22 3250 0403 2084 3273");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(responseEntity.getResponseMessage(), TransactionMessages.INSUFFICIENT_AMOUNT.getErrorMessage());
    }


    @Test
    public void testMissingCurrencyErrorMessage() {
        FundTransferRequestModel request = new FundTransferRequestModel();
        request.setAmount(100.0);
        request.setFromAccount("GB53 REVO 0099 7052 8969 08");
        request.setToAccount("LT00 1234 4567 7890 99");

        Response response = target("api/transfer").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        FundTransferResponseModel responseEntity = response.readEntity(FundTransferResponseModel.class);
        assertEquals(responseEntity.getResponseMessage(), TransactionMessages.REQUIRED_DATA_MISSING.getErrorMessage());
    }
}
