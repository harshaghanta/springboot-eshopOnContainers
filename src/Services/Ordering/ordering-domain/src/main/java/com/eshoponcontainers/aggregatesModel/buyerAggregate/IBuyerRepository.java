package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import com.eshoponcontainers.seedWork.IRepository;

public interface IBuyerRepository extends IRepository<Buyer> {

    Buyer add(Buyer buyer);

    Buyer update(Buyer buyer);

    Buyer find(String buyerIdentityUUID);

    Buyer findById(String id);
}
