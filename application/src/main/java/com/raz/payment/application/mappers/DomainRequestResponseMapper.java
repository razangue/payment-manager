package com.raz.payment.application.mappers;

public interface DomainRequestResponseMapper<D, Req, Resp> {
    Resp domainToResponse(D d);

    D responseToDomain(Resp resp);

    D requestToDomain(Req req);
}
