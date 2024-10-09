package com.javaweb.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "funding_rate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundingRate {
    @Id
    private Long id;

    private String symbol;
    private String fundingRate;
    private String fundingCountdown;
    private String time;
}
