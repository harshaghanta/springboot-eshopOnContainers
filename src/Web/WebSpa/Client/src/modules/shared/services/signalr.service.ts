import { Injectable, NgZone } from '@angular/core';
import { SecurityService } from './security.service';
import { ConfigurationService } from './configuration.service';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';

@Injectable()
export class SignalrService {
    private abortController: AbortController | null = null;
    private reconnectTimer: ReturnType<typeof setTimeout> | null = null;
    private readonly msgSignalrSource = new Subject<any>();
    msgReceived$ = this.msgSignalrSource.asObservable();

    constructor(
        private readonly securityService: SecurityService,
        private readonly configurationService: ConfigurationService, 
        private readonly toastr: ToastrService,
        private readonly ngZone: NgZone,
    ) {
        if (this.configurationService.isReady) {
            this.init();
        } else {
            this.configurationService.settingsLoaded$.subscribe(() => this.init());
        }

        this.securityService.authenticationChallenge$.subscribe((isAuthorized: boolean) => {
            if (isAuthorized) {
                this.init();
                return;
            }

            this.stop();
        });
    }

    private init() {
        if (this.configurationService.isReady && this.securityService.IsAuthorized && !this.abortController) {
            this.connectSse();
        }
    }

    private connectSse() {
        if (this.abortController) {
            return;
        }

        const url = this.configurationService.serverSettings.signalrHubUrl + '/ws/api/notifications/stream';
        const token = this.securityService.GetToken();

        this.abortController = new AbortController();
        this.clearReconnectTimer();

        fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` },
            signal: this.abortController.signal,
        }).then(response => {
            if (!response.ok || !response.body) {
                console.error('SSE connection failed:', response.status);
                this.resetConnection();
                this.scheduleReconnect();
                return;
            }
            console.log('SSE connected');
            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let buffer = '';

            const read = () => {
                reader.read().then(({ done, value }) => {
                    if (done) {
                        console.log('SSE stream ended');
                        this.resetConnection();
                        this.scheduleReconnect();
                        return;
                    }
                    buffer += decoder.decode(value, { stream: true });
                    const lines = buffer.split('\n');
                    buffer = lines.pop() || '';

                    let dataLines: string[] = [];
                    for (const line of lines) {
                        if (line.startsWith('data:')) {
                            dataLines.push(line.substring(5));
                        } else if (line.trim() === '' && dataLines.length > 0) {
                            const data = dataLines.join('\n').trim();
                            dataLines = [];
                            try {
                                const msg = JSON.parse(data);
                                this.handleMessage(msg);
                            } catch {
                                console.warn('Failed to parse SSE data:', data);
                            }
                        }
                    }
                    read();
                }).catch(err => {
                    if (err.name !== 'AbortError') {
                        console.error('SSE read error:', err);
                        this.resetConnection();
                        this.scheduleReconnect();
                    }
                });
            };
            read();
        }).catch(err => {
            if (err.name !== 'AbortError') {
                console.error('SSE fetch error:', err);
                this.resetConnection();
                this.scheduleReconnect();
            }
        });
    }

    private scheduleReconnect() {
        if (this.reconnectTimer) {
            return;
        }

        this.reconnectTimer = setTimeout(() => {
            this.reconnectTimer = null;
            if (this.securityService.IsAuthorized) {
                console.log('SSE reconnecting...');
                this.connectSse();
            }
        }, 5000);
    }

    public stop() {
        this.clearReconnectTimer();
        if (this.abortController) {
            this.abortController.abort();
        }
        this.abortController = null;
    }

    private resetConnection() {
        this.abortController = null;
    }

    private clearReconnectTimer() {
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }
    }

    private handleMessage(msg: any) {
        this.ngZone.run(() => {
            if (msg.notificationType === 'basket-price-change') {
                this.toastr.warning(
                    `Updated from $${msg.oldPrice?.toFixed(2)} to $${msg.newPrice?.toFixed(2)}`,
                    `Basket product ${msg.productId} changed price`
                );
            } else {
                console.log(`Order ${msg.orderId} updated to ${msg.status}`);
                this.toastr.success('Updated to status: ' + msg.status, 'Order Id: ' + msg.orderId);
            }
            this.msgSignalrSource.next(msg);
        });
    }
}