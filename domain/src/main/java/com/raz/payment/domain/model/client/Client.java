package com.raz.payment.domain.model.client;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private String id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private String gender;
    private String nationality;
}
