package com.eshoponcontainers.seedWork;

public interface IRepository<T extends IAggregateRoot> {

    IUnitOfWork getUnitOfWork();
}
