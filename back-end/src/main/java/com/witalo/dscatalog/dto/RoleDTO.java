package com.witalo.dscatalog.dto;

import com.witalo.dscatalog.entites.Role;

import java.io.Serial;
import java.io.Serializable;

public class RoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;

    public RoleDTO() {

    }

    public RoleDTO(Long id,String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
