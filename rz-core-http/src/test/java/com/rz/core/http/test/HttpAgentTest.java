package com.rz.core.http.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.rz.core.http.HttpAgent;
import com.rz.core.http.HttpAgentImpl;
import com.rz.core.http.HttpDecompressionMethods;

public class HttpAgentTest {
    private static HttpAgent httpAgent = new HttpAgentImpl(Arrays.asList(
            // "http://localhost:5000"
            // "http://localhost/YesHJ.NotifyCenter.WebHost"
            "http://qa.notify-center-base.intra.yeshj.com"
    // "http://10.20.10.57:8033"
    // "http://ptpass.hjapi.com/center"
    // "http://ptpass.hjapi.com"
    // "http://localhost"
    // "http://10.20.10.57:8034"
    ), 20 * 1000, null, HttpDecompressionMethods.Both, "application/json", StandardCharsets.UTF_8.name(), "application/json;charset=utf-8");

    public static void main(String[] args) {
        HttpAgentTest HttpAgentTest = new HttpAgentTest();
        try {
            HttpAgentTest.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End DateTimeUtilsTest...");
    }

    private void test() throws IOException {
        HttpAgentImpl.getHttpAgent().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx00ec802f42606bb1&corpsecret=04zkSPCisPiHEOOdUt1nFyX5akAD81r4Slf20oocLDMms11H5EpoGNUYWEQAtEX1");
        HttpAgentImpl.getHttpAgent().deleteEx("asdasd", "asdasdasd");
        httpAgent.hashCode();
    }
}
