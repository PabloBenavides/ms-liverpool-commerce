package com.liverpool.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LastModification {
    private LocalDateTime modificationDate;
    private String modifiedBy;
    private String lastAction;
}
