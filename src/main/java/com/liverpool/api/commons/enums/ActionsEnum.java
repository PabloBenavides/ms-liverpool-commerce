package com.liverpool.api.commons.enums;

import lombok.Getter;

@Getter
public enum ActionsEnum {
    CREATED("CREATED"),
    UPDATED("UPDATED");

    private final String actionText;

    ActionsEnum(String actionText) {
        this.actionText = actionText;
    }

}
