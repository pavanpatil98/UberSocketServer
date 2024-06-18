package com.example.ubersocketserver.dto;

import com.example.ubersocketserver.models.ExactLocation;
import lombok.*;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RideRequestDto {
    private Long passengerId;

    private Long bookingId;

    //private ExactLocation startLocation;

    //private ExactLocation endLocation;

    private List<Long> driverIds;
}
