package com.ah.utils;

import com.ah.pojo.GameOrder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.io.IOException;
import java.util.UUID;

@Component
public class Alipay {
    //    @Value("${Alipay.serverUrl}")
    private String serverUrl = "https://openapi.alipaydev.com/gateway.do";
    //    @Value("${Alipay.APP_ID}")
    private String APP_ID = "2021000116659971";
    //    @Value("${Alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCIzbUzoWd4EfYZ9wcxyfXKDSztIDJftJ4kLbDrx4D2qfx0eCoYi20XKsEM2kpk5Sdiy/uEQc6HPLFd+KCC/PmKz+iIK/3hq/TlFF9RD6J1VcQ29UVrllZUhUMM0Z4DCxWB3c8q5l0Wwng+Oxgfyt2JE1SM8PDJKavNUuTX5RUHk67cw6j1l7DPfOHyOW1c7HM6KleJ1U56Z/SvAfMNsppresvaVD/0sf4XwE6h1VxaQC/mwQ6CeOHoGEroWLfH1MtwtlMPoua86mItdWoFY3ia4MIIfb1GCCkkmE9YwGaG3RJITO/BxloRDrIgmvGvZMW2eGEfs7SqK7l5RHqrNwaxAgMBAAECggEAN+OCQaOQ99auqhDnmYmepnsXfZCHw92wNauM+eY5Y4wynQpyeDCN0sceszeC6RFNM+mNyYxw6YL8Gx31xU3ATNt/bhI62DU+CVB3WFCA2Fq/nIw7Xia/AiynJdeFLE0c7j9F4YrXQPMV2Kafxm1EcDm7cxED7creazWQVDs8iilKH9PXUAljhmGhAUwbFwz00p5yE9/v4lW/6pO4u2xrPk6KFXEeVgjcY8KKVJtRJ7IXakhmeWgmHsr1zLyQtJFvl9XrrBbxRcppntwAXxO8TF5Yh9QbhDyA1EQfnZp6XSDEUeQhL2iNnAyBxFwwdBoXEHk6AI+sQKmA40Ea9BOcmQKBgQDfLC/PUzxBHqrylwwkof7/lVuonp6Cj5c/sk9ivr9HEfTUFQJLJmTtPLGipPJsiTDw3Qoth1+khLGjDKeJpnT1ooEuRUTnKM5hXTetQzjBKo1t6bgil+KQJdj8gz6tA2aMYdteMxBX6o5CKbCJa+5VolGB35t5SlICjXS5kCqr7wKBgQCc7TKn70NnIDNfaIOe8eL+HiF/mvlCzXIfQCrCMH7hC16b4oJvbkq/dk7sob/eD4+R/XVRXmYF9fMW7XkZAfkV6bEpSMidhxvWMfalMEa3+SUF7KnYBg4kgmZ6ppjYP30p/RS8L+bIEWhrrDPQFGVK3CNA1lQKfiTG1+ePfT1XXwKBgD4sfz1RQQ4bTyC6eAWUpAfsQlbdqedRvZ9fO8KUxZE1LXIQUE1Dawa0zAA119vo9JuF2RoKBtk8poh4NAyjNR5hoX+UiLelpKl3YNWCTsNx6Wjfng71JYdnI+ZlYgatVdpyQL0jmMAFdpB0MNx4FzQAPpjiBNxrmiFf607vHRQ9AoGASWoevLoOynwiDIyUpxN6om/uNAiIfFwo+73FQzv038u14y/A3ei3R4NNwR6a6IRXInF1JTAIgtlhdGR8ttpZcYr2ut22HRxMRZTyUq2XKBKC45aojsumaiYnU1IFR4XMRIyF2tR+HzMlqtnRE5O3H6KFS5v5jT8ebXgseNCalfkCgYBaFGGeWEjhccLRwtURnKXtlcXwkZT68WZmZ7EPakHUdnGbJwCGageKYlMH83odQei+88So0v4f3H5MAqvBswhWGAa/XyO/cIG2ChEVR5VTyKBqsAICeopuPl297M10wMHnhndRret5FDHFoZLc1+Ow94Ik4ZzQgJcCRxQJfXvPLQ==";
    private String FORMAT = "json";
    //    @Value("${Alipay.CHARSET}")
    private String CHARSET = "UTF-8";
    //    @Value("${Alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvmUDFx5FQ1dLFDbWA0SsFIypl0wj/uGs0DOHMeoweSrQ3rcOe28XXgk8uJWYdZiT5/BqRg41YiqiJmn5VbqnIaZ46pxEQ98xV4S4AUEVPIzRPHvUpdsj6Tt/x8v/0+KxebBeP+iNH71e+19eio/btSqd077yqclEaqG4M1qBZf0/DLAY+LbN2KRwN/gzK3GnN7FoaV4ut4LjxEDGeOj4pmNeBywdJHcf597zmnWNEaoXCWXFoE2z2WLI2Ddoq/d1JNkB1R/l+3t/bMxHvYo0Ju55XD/omPoZYYaHPM5EgDT0KRRAuKemIdpp63tdvpkH5uUm7Ov5vZSUaKHyySt19wIDAQAB";
    //    @Value("${Alipay.SIGN_TYPE}")
    private String SIGN_TYPE = "RSA2";


    public String pay(HttpServletRequest httpRequest, HttpServletResponse httpResponse, GameOrder book) throws ServletException, IOException {

        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);  //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://4dslcsft.shenzhuo.vip:11239/returnUrl"); //在公共参数中设置回跳和通知地址
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        String replace = s.replace("-", "");
        int i = Integer.parseInt(book.getPrice());
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + replace + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":\"" + i * book.getNum() + "\"," +
                "    \"subject\":\"" + book + "\"," +
                "    \"body\":\"" + book + "\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }" +
                "  }"); //填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        /*httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();*/
        return form;

    }

}
