package com.vfasad.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    CREATED("Новый"),
    IN_PROGRESS("В работе"),
    SHIPPING("Отгружается"),
    CLOSED("Закрыт"),
    BLOCKED("Заблокирован"),
    CANCELLED("Отменен");

    String title;

    OrderStatus(String title) {
        this.title = title;
    }
}
