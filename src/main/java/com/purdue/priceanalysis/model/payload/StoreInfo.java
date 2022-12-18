package com.purdue.priceanalysis.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString
public class StoreInfo {
    public String store_name;
    public StoreDetail store_detail;
}
