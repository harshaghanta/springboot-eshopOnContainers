import { Injectable, NgZone } from '@angular/core';
import { SecurityService } from './security.service';
import { ConfigurationService } from './configuration.service';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';

@Injectable()
export class SignalrService {
    private abortController: AbortController | null = null;
    private msgSignalrSource = new Subject();
    msgReceived$ = this.msgSignalrSource.asObservable();

    constructor(
        private securityService: SecurityService,
        private configurationService: ConfigurationService, 
        private toastr: ToastrService,
        private ngZone: NgZone,
    ) {
        if (this.configurationService.isReady) {
            this.init();
        } else {
            this.configurationService.settingsLoaded$.subscribe(() => this.init());
        }
    }

    private init() {
        if (this.securityService.IsAuthorized) {
            this.connectSse();
        }
    }

    private connectSse() {
        const url = this.configurationService.serverSettings.signalrHubUrl + '/ws/api/notifications/stream';
        const token = this.securityService.GetToken();

        this.abortController = new AbortController();

        fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` },
            signal: this.abortController.signal,
        }).then(response => {
            if (!response.ok || !response.body) {
                console.error('SSE connection failed:', response.status);
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
                                this.ngZone.run(() => {
                                    console.log(`Order ${msg.orderId} updated to ${msg.status}`);
                                    this.toastr.success('Updated to status: ' + msg.status, 'Order Id: ' + msg.orderId);
                                    this.msgSignalrSource.next();
                                });
                            } catch (e) {
                                console.warn('Failed to parse SSE data:', data);
                            }
                        }
                    }
                    read();
                }).catch(err => {
                    if (err.name !== 'AbortError') {
                        console.error('SSE read error:', err);
                        this.scheduleReconnect();
                    }
                });
            };
            read();
        }).catch(err => {
            if (err.name !== 'AbortError') {
                console.error('SSE fetch error:', err);
                this.scheduleReconnect();
            }
        });
    }

    private scheduleReconnect() {
        setTimeout(() => {
            if (this.securityService.IsAuthorized) {
                console.log('SSE reconnecting...');
                this.connectSse();
            }
        }, 5000);
    }

    public stop() {
        if (this.abortController) {
            this.abortController.abort();
            this.abortController = null;
        }
    }
}