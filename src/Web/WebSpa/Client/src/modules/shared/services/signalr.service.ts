import { Injectable } from '@angular/core';
import { SecurityService } from './security.service';
import { ConfigurationService } from './configuration.service';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { Client, Message } from '@stomp/stompjs';

@Injectable()
export class SignalrService { // Renamed for clarity
    private stompClient: Client;
    private msgSignalrSource = new Subject();
    msgReceived$ = this.msgSignalrSource.asObservable();

    constructor(
        private securityService: SecurityService,
        private configurationService: ConfigurationService, 
        private toastr: ToastrService,
    ) {
        if (this.configurationService.isReady) {
            this.init();
        } else {
            this.configurationService.settingsLoaded$.subscribe(() => this.init());
        }
    }

    private init() {
        if (this.securityService.IsAuthorized) {
            this.setupStompClient();
        }
    }

    private setupStompClient() {
        const url = this.configurationService.serverSettings.signalrHubUrl + '/ws'; // Your Spring WebSocket endpoint
        
        this.stompClient = new Client({
            webSocketFactory: () => new SockJS(url),
            connectHeaders: {
                Authorization: `Bearer ${this.securityService.GetToken()}`
            },
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        this.stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            this.registerHandlers();
        };

        this.stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
        };

        this.stompClient.activate();
    }

    private registerHandlers() {
        // Subscribing to a specific user's notification topic
        this.stompClient.subscribe('/user/queue/notifications', (message: Message) => {
            const msg = JSON.parse(message.body);
            console.log(`Order ${msg.orderId} updated to ${msg.status}`);
            this.toastr.success('Updated to status: ' + msg.status, 'Order Id: ' + msg.orderId);
            this.msgSignalrSource.next();
        });
    }

    public stop() {
        if (this.stompClient) {
            this.stompClient.deactivate();
        }
    }
}