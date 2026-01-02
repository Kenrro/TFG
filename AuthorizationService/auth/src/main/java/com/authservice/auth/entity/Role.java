package com.authservice.auth.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public enum Role {
    ADMIN("ADMIN", "Administrator", "Has full access to all resources"),
    SELLER("SELLER", "Seller", "Can manage own products and view sales data"),
    CUSTOMER("CUSTOMER", "Customer", "Can browse products and make purchases")
    ;
    private String code;
    private String displayName;
    private String description;
    public String getCode() {
        return code;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getDescription() {
        return description;
    }
}
