package com.raz.payment.domain.interfaces;

import com.raz.payment.domain.model.client.OperationDetail;

public interface OperationDetailRepository {
    OperationDetail save(OperationDetail operationDetail);
}
