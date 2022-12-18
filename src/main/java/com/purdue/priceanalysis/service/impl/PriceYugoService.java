package com.purdue.priceanalysis.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.purdue.priceanalysis.common.util.LoggerUtil;
import com.purdue.priceanalysis.common.util.Utils;
import com.purdue.priceanalysis.model.payload.ProductResponse;
import com.purdue.priceanalysis.model.payload.SearchResponse;
import com.purdue.priceanalysis.model.payload.StoreDetail;
import com.purdue.priceanalysis.model.payload.StoreInfo;
import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.stylesheets.LinkStyle;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceYugoService {

    private final static Logger LOGGER = LoggerUtil.getLogger(LoggerUtil.LogType.ROOT_LOGGER);

    private String priceYugoBaseUrl = "https://price-api.datayuge.com/api/v1/compare/";
    private String API_KEY = "3f9dzFuxdVPcqKknGmvHkgCz6m2f0NoevRR";

    private RestTemplate restTemplate;

    public PriceYugoService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<SearchResponse> searchProduct(String productName) throws Exception {
        String searchURL = "search";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(priceYugoBaseUrl + searchURL)
                .queryParam("api_key", API_KEY)
                .queryParam("product", productName);

        UriComponents uri = uriComponentsBuilder.build();

        JsonNode root = getResponse(uri.toUriString());

        ArrayNode searchData = (ArrayNode) root.get("data");

        List<SearchResponse> searchResponse = new ArrayList<>();
        for (JsonNode element : searchData) {
            searchResponse
                    .add(new SearchResponse(
                            element.get("product_title").asText(),
                            element.get("can_compare").asBoolean(),
                            element.get("product_lowest_price").asInt(),
                            element.get("is_available").asBoolean(),
                            element.get("product_link").asText(),
                            element.get("product_id").asText(),
                            element.get("product_category").asText(),
                            element.get("product_sub_category").asText(),
                            element.get("product_image").asText()
                            ));
        }

        return searchResponse;

    }

    public ProductResponse productDetail(String id) throws Exception {
        String detailURL = "detail";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(priceYugoBaseUrl + detailURL)
                .queryParam("api_key", API_KEY)
                .queryParam("id", id);

        UriComponents uri = uriComponentsBuilder.build();

        JsonNode root = getResponse(uri.toUriString());

        ArrayNode storesJN = (ArrayNode) root.get("data").get("stores");

        List<StoreInfo> storeInfoList = new ArrayList<>();
        for (JsonNode element : storesJN) {
            String storeName = element.fields().next().getKey();

            if (element.get(storeName).size() != 0)
                try {
                    storeInfoList.add(new StoreInfo(storeName, transformIntoStoreDetail(element.get(storeName))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            /*
            if (element.get("amazon") != null)
                try {
                    storeInfoList.add(new StoreInfo("Amazon", transformIntoStoreDetail(element.get("amazon"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (element.get("ebay") != null)
                try {
                    storeInfoList.add(new StoreInfo("Ebay", transformIntoStoreDetail(element.get("ebay"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            */

        }

        return new ProductResponse(storeInfoList);

    }

    public StoreDetail transformIntoStoreDetail(JsonNode node) {
        LOGGER.info("Transforming Product info: " + node.toPrettyString());
        return new StoreDetail(Utils.isEmpty(node.get("product_store")) ? "" : node.get("product_store").asText(),
                Utils.isEmpty(node.get("product_store_logo")) ? "" : node.get("product_store_logo").asText(),
                Utils.isEmpty(node.get("product_store_url")) ? "" : node.get("product_store_url").asText(),
                Utils.isEmpty(node.get("product_price")) ? 0.0 : node.get("product_price").asDouble(),
                Utils.isEmpty(node.get("product_offer")) ? "" : node.get("product_offer").asText(),
                Utils.isEmpty(node.get("product_color")) ? "" : node.get("product_color").asText(),
                Utils.isEmpty(node.get("product_delivery")) ? "" : node.get("product_delivery").asText(),
                Utils.isEmpty(node.get("product_delivery_cost")) ? 0.0 : node.get("product_delivery_cost").asDouble(),
                Utils.isEmpty(node.get("is_emi")) ? null : node.get("is_emi").asText().equals("1") ? true : false,
                Utils.isEmpty(node.get("is_cod")) ? null : node.get("is_cod").asText().equals("1") ? true : false,
                Utils.isEmpty(node.get("return_time")) ? "" : node.get("return_time").asText());
    }

    public JsonNode getResponse(String url) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.getBody());
    }

    public HttpHeaders createBasicAuthHeader() {
        return new HttpHeaders() {{
        }};
    }
}
