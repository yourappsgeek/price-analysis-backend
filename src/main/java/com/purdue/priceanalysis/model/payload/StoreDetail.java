package com.purdue.priceanalysis.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString
@NoArgsConstructor
public class StoreDetail {
    public String product_store;
    public String product_store_logo;
    public String product_store_url;
    public Double product_price;
    public String product_offer;
    public String product_color;
    public String product_delivery;
    public Double product_delivery_cost;
    public boolean emi;
    public boolean cod;
    public String return_time;
}
