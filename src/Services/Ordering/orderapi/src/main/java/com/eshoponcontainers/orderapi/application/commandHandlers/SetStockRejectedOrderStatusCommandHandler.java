package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.orderapi.aop.MyTransactional;
import com.eshoponcontainers.orderapi.application.commands.SetStockRejectedOrderStatusCommand;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetStockRejectedOrderStatusCommandHandler
        implements Command.Handler<SetStockRejectedOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;
    private final Pipeline pipeline;
    @Override
    // @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Boolean handle(SetStockRejectedOrderStatusCommand command) {
        // Simulate a work time for validating the payment
        // try {
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        TransactionContext.beginTransactionContext();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Transaction completed with COMMIT status.");
                    var domainEvents = DomainContext.getDomainEvents();
                    DomainContext.clearContext(); // Safety: clear after sending
                    if (domainEvents != null) {
                        domainEvents.forEach(event -> {
                            log.info("Publishing domain event: {}", event.getClass().getSimpleName());
                            pipeline.send(event);
                        });

                    }
                } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.error("Transaction completed with ROLLBACK status.");
                } else {
                    log.info("Transaction completed with unknown status: {}", status);
                }
                TransactionContext.clearContext();
            }
        });
        
        Order order = orderRepository.get(command.getOrderNumber());
        if (order == null)
            return false;

        order.SetCancelledStatusWhenStockIsRejected(command.getOrderStockItems());
        return true;
        // return orderRepository.getUnitOfWork().saveChanges();
    }

}
