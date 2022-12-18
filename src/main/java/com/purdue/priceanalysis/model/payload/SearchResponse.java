package com.purdue.priceanalysis.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString
public class SearchResponse {

    public String product_title;
    public boolean can_compare;
    public int product_lowest_price;
    public boolean is_available;
    public String product_link;
    public String product_id;
    public String product_category;
    public String product_sub_category;
    public String product_image;

}
