package com.purdue.priceanalysis.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class ProductResponse {

    private List<StoreInfo> store_info;

}
