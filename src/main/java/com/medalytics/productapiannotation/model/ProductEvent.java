package com.medalytics.productapiannotation.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductEvent {

    private Long eventId;
    private String eventType;
}
