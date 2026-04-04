import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { Observable } from 'rxjs';

import { BasketService } from './basket.service';
import { IBasket } from '../shared/models/basket.model';
import { IBasketItem } from '../shared/models/basketItem.model';
import { BasketWrapperService } from '../shared/services/basket.wrapper.service';
import { SignalrService } from '../shared/services/signalr.service';

@Component({
    selector: 'esh-basket .esh-basket .mb-5',
    styleUrls: ['./basket.component.scss'],
    templateUrl: './basket.component.html'
})
export class BasketComponent implements OnInit, OnDestroy {
    errorMessages: any;
    basket: IBasket;
    totalPrice: number = 0;
    private readonly refreshIntervalMs = 10000;
    private refreshHandle: ReturnType<typeof setInterval> | null = null;
    private readonly notifiedPriceChanges = new Set<string>();

    constructor(private readonly basketSerive: BasketService, private readonly router: Router,
        private readonly basketWrapperService: BasketWrapperService,
        private readonly signalrService: SignalrService,
        private readonly toastr: ToastrService) { }

    ngOnInit() {
        this.loadBasket(false);
        this.signalrService.msgReceived$
            .subscribe((message: any) => {
                if (message?.notificationType === 'basket-price-change') {
                    this.loadBasket(false);
                }
            });

        this.refreshHandle = setInterval(() => this.loadBasket(true), this.refreshIntervalMs);
    }

    ngOnDestroy() {
        if (this.refreshHandle) {
            clearInterval(this.refreshHandle);
            this.refreshHandle = null;
        }
    }

    deleteItem(id: string) {
        this.basket.items = this.basket.items.filter(item => item.id !== id);
        this.calculateTotalPrice();
        
        this.basketSerive.setBasket(this.basket).subscribe(x => 
            {
                this.basketSerive.updateQuantity();
                console.log('basket updated: ' + x)
            }
        );
    }

    itemQuantityChanged(item: IBasketItem, quantity: number) {
        item.quantity = quantity > 0 ? quantity : 1;
        this.calculateTotalPrice();
        this.basketSerive.setBasket(this.basket).subscribe(x => console.log('basket updated: ' + x));
    }

    update(event: any): Observable<boolean> {
        let setBasketObservable = this.basketSerive.setBasket(this.basket);
        setBasketObservable
            .subscribe(
            x => {
                this.errorMessages = [];
                console.log('basket updated: ' + x);
            },
            errMessage => this.errorMessages = errMessage.messages);
        return setBasketObservable;
    }

    checkOut(event: any) {
        this.update(event)
            .subscribe(
                x => {
                    this.errorMessages = [];
                    this.basketWrapperService.basket = this.basket;
                    this.router.navigate(['order']);
        });
    }

    private calculateTotalPrice() {
        this.totalPrice = 0;
        this.basket.items.forEach(item => {
            this.totalPrice += (item.unitPrice * item.quantity);
        });
    }

    private loadBasket(showToastForDetectedChanges: boolean) {
        this.basketSerive.getBasket().subscribe(basket => {
            this.basket = basket;
            if (showToastForDetectedChanges) {
                this.notifyDetectedPriceChanges();
            }
            this.calculateTotalPrice();
        });
    }

    private notifyDetectedPriceChanges() {
        this.basket?.items?.forEach(item => {
            if (!item || item.oldUnitPrice <= 0 || item.oldUnitPrice === item.unitPrice) {
                return;
            }

            const notificationKey = `${item.productId}:${item.oldUnitPrice}:${item.unitPrice}`;
            if (this.notifiedPriceChanges.has(notificationKey)) {
                return;
            }

            this.notifiedPriceChanges.add(notificationKey);
            this.toastr.warning(
                `Updated from $${item.oldUnitPrice.toFixed(2)} to $${item.unitPrice.toFixed(2)}`,
                `Basket product ${item.productId} changed price`
            );
        });
    }
}
