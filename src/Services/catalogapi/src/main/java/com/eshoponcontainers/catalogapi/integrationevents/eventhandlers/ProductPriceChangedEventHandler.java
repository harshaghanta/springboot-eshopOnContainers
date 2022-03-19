package com.eshoponcontainers.catalogapi.integrationevents.eventhandlers;

import com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

public class ProductPriceChangedEventHandler implements IntegrationEventHandler<ProductPriceChangedIntegrationEvent> {

  @Override
  public Runnable handle(ProductPriceChangedIntegrationEvent event) {
      
       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Price changed from " + event.getOldPrice() + " to " + event.getNewPrice());                
            }
        };

        runnable.run();
        return runnable;
  }
    
}
