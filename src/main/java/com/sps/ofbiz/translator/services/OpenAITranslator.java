package com.sps.ofbiz.translator.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAITranslator {


        @Value("${OpenAI.apiKey}")
        public String apiKey;

        public String translate(String englishText, String toLang, int maxTokens) throws Exception {

                HttpPost postMethod = new HttpPost("https://api.openai.com/v1/completions");
                Map<String, Object> body = new HashMap<>();
                body.put("model", "gpt-3.5-turbo-instruct");
                body.put("prompt", "Translate the following English text to " + toLang + ": " + englishText);
                body.put("temperature", 0.7);
                body.put("max_tokens", maxTokens);
                JSONObject info = new JSONObject(body);
                StringEntity requestEntity = new StringEntity(
                                info.toString(),
                                ContentType.APPLICATION_JSON);
                Logger.getGlobal().info("Sending: " + info.toString());
                postMethod.setEntity(requestEntity);
                postMethod.addHeader("Authorization", "Bearer " + apiKey);

                CloseableHttpResponse res = (CloseableHttpResponse) HttpClients.createDefault().execute(postMethod);
                if (res.getCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = res.getEntity();

                        String retSrc = EntityUtils.toString(entity);

                        JSONObject result = new JSONObject(retSrc);
                        String response = result.getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getString("text").trim();
                        return response;

                } else {
                        Logger.getGlobal().info("StatusCode: " + res.getCode() + "");
                        Logger.getGlobal().info("GPT RF: " + res.getReasonPhrase());
                        Logger.getGlobal().info("GPT EU: " + EntityUtils.toString(res.getEntity()));
                }

                return "";

        }

}
