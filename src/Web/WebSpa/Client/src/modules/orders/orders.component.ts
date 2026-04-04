import { Component, OnDestroy, OnInit }    from '@angular/core';
import { OrdersService }        from './orders.service';
import { IOrder }               from '../shared/models/order.model';
import { ConfigurationService } from '../shared/services/configuration.service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SignalrService } from '../shared/services/signalr.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'esh-orders .esh-orders .mb-5',
    styleUrls: ['./orders.component.scss'],
    templateUrl: './orders.component.html'
})
export class OrdersComponent implements OnInit, OnDestroy {
    private oldOrders: IOrder[];
    private readonly refreshIntervalMs = 10000;
    private refreshHandle: ReturnType<typeof setInterval> | null = null;
    errorReceived: boolean;

    orders: IOrder[];

    constructor(private readonly service: OrdersService, private readonly configurationService: ConfigurationService,
        private readonly signalrService: SignalrService,
        private readonly toastr: ToastrService) { }

    ngOnInit() {
        if (this.configurationService.isReady) {
            this.getOrders(false);
        } else {
            this.configurationService.settingsLoaded$.subscribe(x => {
                this.getOrders(false);
            });
        }

        this.signalrService.msgReceived$
            .subscribe((message: any) => {
                if (!message || message.notificationType === 'order-status') {
                    this.getOrders(false);
                }
            });

        this.refreshHandle = setInterval(() => this.getOrders(true), this.refreshIntervalMs);
    }

    ngOnDestroy() {
        if (this.refreshHandle) {
            clearInterval(this.refreshHandle);
            this.refreshHandle = null;
        }
    }

    getOrders(showToastForDetectedChanges: boolean) {
        this.errorReceived = false;
        this.service.getOrders()
            .pipe(catchError((err) => this.handleError(err)))
            .subscribe(orders => {
                if (showToastForDetectedChanges) {
                    this.notifyDetectedStatusChanges(orders);
                }
                this.orders = orders;
                this.oldOrders = this.orders;
                console.log('orders items retrieved: ' + orders.length);
        });
    }

    private notifyDetectedStatusChanges(orders: IOrder[]) {
        const previousOrders = new Map((this.oldOrders || []).map(order => [String(order.ordernumber), order]));

        (orders || []).forEach((order: any) => {
            const orderNumber = String(order.ordernumber);
            const previousOrder: any = previousOrders.get(orderNumber);

            if (!previousOrder && order.status?.toLowerCase() === 'submitted') {
                this.toastr.info(`Order ${orderNumber} was submitted`, 'Order update');
                return;
            }

            if (previousOrder && previousOrder.status !== order.status) {
                this.toastr.info(`Order ${orderNumber} updated to ${order.status}`, 'Order update');
            }
        });
    }

    cancelOrder(orderNumber) {
        this.errorReceived = false;
        this.service.cancelOrder(orderNumber)
            .pipe(catchError((err) => this.handleError(err)))
            .subscribe(() => {
                console.log('order canceled: ' + orderNumber);
        });
    }

    private handleError(error: any) {
        this.errorReceived = true;
        return Observable.throw(error);
    }  
}

