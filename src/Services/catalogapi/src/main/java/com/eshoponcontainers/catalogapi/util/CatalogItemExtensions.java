package com.eshoponcontainers.catalogapi.util;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;

public class CatalogItemExtensions {
    
    public static void fillProductUrl(CatalogItem item, String picBaseUrl) {
        item.setPictureUri(picBaseUrl.replaceAll("[0]",  item.getId().toString()));
    }
}
