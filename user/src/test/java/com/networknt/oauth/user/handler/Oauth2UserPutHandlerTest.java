package com.networknt.oauth.user.handler;

import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.exception.ClientException;
import com.networknt.status.Status;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
* Generated by swagger-codegen
*/
public class Oauth2UserPutHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(Oauth2UserPutHandlerTest.class);

    @Test
    public void testOauth2UserPutHandler() throws ClientException, ApiException, UnsupportedEncodingException {
        String user = "{\"firstName\":\"Steve\",\"lastName\":\"Hu\",\"userType\":\"partner\",\"userId\":\"test01\",\"email\":\"test01@gmail.com\"}";
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPut httpPut = new HttpPut("http://localhost:6885/oauth2/user");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(new StringEntity(user));

        try {
            CloseableHttpResponse response = client.execute(httpPut);
            logger.debug("StatusCode = " + response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            logger.debug("Response body = " + body);
            Assert.assertNotNull(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserNotFound() throws ClientException, ApiException, UnsupportedEncodingException {
        String user = "{\"userId\":\"fake\",\"userType\":\"employee\",\"firstName\":\"Steve\",\"lastName\":\"Hu\",\"email\":\"abc@networknt.com\"}";
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPut httpPut = new HttpPut("http://localhost:6885/oauth2/user");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(new StringEntity(user));
        try {
            CloseableHttpResponse response = client.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(404, statusCode);
            if(statusCode == 404) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12013", status.getCode());
                Assert.assertEquals("USER_NOT_FOUND", status.getMessage()); // response_type missing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}