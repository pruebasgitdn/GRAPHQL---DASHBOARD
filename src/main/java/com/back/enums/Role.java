package com.back.enums;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    OWNER,
    ADMIN,
    MEMBER,
    GUEST

}
