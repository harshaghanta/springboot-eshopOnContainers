import http from 'k6/http';
import { check, sleep, group } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 20 },
        { duration: '10s', target: 0 },
    ],
};

export default function () {
    group('products endpoint', function () {
        let res = http.get('http://eshop.ghantas.com/webshoppingapigw/c/api/v1/catalog/items?pageIndex=0&pageSize=12');
        check(res, { 'status is 200': (r) => r.status === 200 });
        sleep(1);

        res = http.get('http://eshop.ghantas.com/webshoppingapigw/c/api/v1/catalog/catalogtypes');
        check(res, { 'status is 200': (r) => r.status === 200 });
        sleep(1);

        res = http.get('http://eshop.ghantas.com/webshoppingapigw/c/api/v1/catalog/catalogbrands');
        check(res, { 'status is 200': (r) => r.status === 200 });
        sleep(1);
    });

    
}