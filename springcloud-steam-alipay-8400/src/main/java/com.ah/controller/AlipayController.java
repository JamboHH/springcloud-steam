package com.ah.controller;

import com.ah.client.GameOrderClient;
import com.ah.pojo.GameOrder;
import com.ah.utils.Alipay;
import com.ah.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
public class AlipayController {

    @Autowired
    Alipay alipay;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtils redisUtils;

    /*    @Autowired
        GameOrderService gameOrderService;*/
    @Autowired
    private GameOrderClient gameOrderClient;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public void pay(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws ServletException, IOException {

        Object id = redisUtils.get((String) map.get("id"));
        Object o = JSONObject.toJSON(id);
        GameOrder gameOrder = JSONObject.parseObject(o.toString(), GameOrder.class);
        if (gameOrder != null) {
            String form = alipay.pay(request, response, gameOrder);
            response.getWriter().write(form); //直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            response.getWriter().write("支付失败！！！！"); //直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        }

    }


    @Value("${Alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${Alipay.CHARSET}")
    private String CHARSET;
    @Value("${Alipay.SIGN_TYPE}")
    private String SIGN_TYPE;


    @RequestMapping("/returnUrl")
    public void returnUrl(HttpServletRequest request) throws AlipayApiException {
        System.out.println(" ==---------------=== ");
        Map<String, String> stringStringMap = convertRequestParamsToMap(request);
        boolean signVerified = AlipaySignature.rsaCheckV1(stringStringMap, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); //调用SDK验证签名

        if (signVerified) {
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("stringStringMap = " + stringStringMap);
            if (stringStringMap.get("trade_status").equals("TRADE_SUCCESS")) {
                //out_trade_no订单已支付，修改数据库
                Object out_trade_no = redisUtils.get(stringStringMap.get("out_trade_no"));
                Object o = JSONObject.toJSON(out_trade_no);
                GameOrder gameOrder = JSONObject.parseObject(o.toString(), GameOrder.class);
                gameOrder.setStatus("TRADE_SUCCESS");
                gameOrderClient.addOrder(gameOrder);
//                gameOrderService.addOrder(gameOrder);
                redisUtils.del(stringStringMap.get("out_trade_no"));
            }
        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure。
        }

    }


    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }
}
