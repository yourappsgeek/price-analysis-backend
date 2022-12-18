package com.purdue.priceanalysis.controller;

import com.purdue.priceanalysis.common.enums.ResponseCode;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.model.payload.SearchResponse;
import com.purdue.priceanalysis.service.impl.PriceYugoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("price-analysis")
public class PriceAnalysisController {

    private PriceYugoService priceYugoService;

    public PriceAnalysisController(PriceYugoService priceYugoService) {
        this.priceYugoService = priceYugoService;
    }

    @GetMapping("search")
    public ApiResponse searchProduct(@RequestParam(required = true) String product) {

        try {
            return new ApiResponse<>(ResponseCode.OPERATION_SUCCESSFUL.getCode(),
                    ResponseCode.OPERATION_SUCCESSFUL.getMessage(),
                    priceYugoService.searchProduct(product));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(ResponseCode.OPERATION_FAILED.getCode(),
                    ResponseCode.OPERATION_FAILED.getMessage());
        }
    }

    @GetMapping("detail")
    public ApiResponse productDetail(@RequestParam(required = true) String id) {

        try {
            return new ApiResponse<>(ResponseCode.OPERATION_SUCCESSFUL.getCode(),
                    ResponseCode.OPERATION_SUCCESSFUL.getMessage(),
                    priceYugoService.productDetail(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(ResponseCode.OPERATION_FAILED.getCode(),
                    ResponseCode.OPERATION_FAILED.getMessage());
        }
    }
}
