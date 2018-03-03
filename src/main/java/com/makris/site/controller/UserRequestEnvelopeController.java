package com.makris.site.controller;

import com.makris.site.service.UserSnatchEnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserRequestEnvelopeController {
    @Autowired
    UserSnatchEnvelopeService userSnatchEnvelopeService;

    @RequestMapping(value = "snatchEnvelope", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> snatchEnvelope(@RequestParam("envelopeId") long envelopeId,
                                            @RequestParam("userId") long userId){
        int result = userSnatchEnvelopeService.snatchEnvelope(envelopeId, userId);
        Map<String, Object> resultMap = new HashMap<>();
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "搶紅包成功" : "搶紅包失敗");
        return resultMap;
    }

    @RequestMapping(value = "snatchEnvelopeByRedis", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> snatchEnvelopeByRedis(@RequestParam("envelopeId") long envelopeId,
                                                     @RequestParam("userId") long userId){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        long result = userSnatchEnvelopeService.grabEnvelopeByRedis(envelopeId, userId);
        boolean flag = result > 0;
        resultMap.put("result", flag);
        resultMap.put("message", flag ? "搶紅包成功": "搶紅包失敗");
        return resultMap;
    }
}
