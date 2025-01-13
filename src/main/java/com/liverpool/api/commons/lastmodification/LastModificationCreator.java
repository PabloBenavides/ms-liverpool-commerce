package com.liverpool.api.commons.lastmodification;

import com.liverpool.api.dto.LastModification;

import java.time.LocalDateTime;

public class LastModificationCreator {
    public static LastModification createLastModification(String action, String user) {
        return LastModification.builder()
                .modificationDate(LocalDateTime.now())
                .modifiedBy(user)
                .lastAction(action)
                .build();
    }
}
