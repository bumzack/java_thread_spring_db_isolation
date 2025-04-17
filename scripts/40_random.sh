#!/bin/zsh

#  -H "content-type: application/json"

for i in $(seq 0 50)
do
        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 0,  "productCode": "c1" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 0  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 1,  "productCode": "c1" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 1  }'  -H "content-type: application/json"   &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 2  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 2,  "productCode": "c2" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 3  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 3,  "productCode": "c3" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 4  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 4,  "productCode": "c4" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 5  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 5,  "productCode": "c5" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 6  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 6,  "productCode": "c4" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 7  }'  -H "content-type: application/json"   &

        curl -X PUT http://localhost:8080/api/carts -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 7,  "productCode": "c5" }'  -H "content-type: application/json"  &
        curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 8  }'  -H "content-type: application/json"   &

      echo "done request ${i}"
done
