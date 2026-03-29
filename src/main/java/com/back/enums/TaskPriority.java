package com.back.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskPriority {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED
}
