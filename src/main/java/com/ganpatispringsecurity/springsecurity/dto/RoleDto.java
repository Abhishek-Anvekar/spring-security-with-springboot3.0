package com.ganpatispringsecurity.springsecurity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private String roleId;
    private String roleName;
}
