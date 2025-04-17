#!/bin/zsh

#  -H "content-type: application/json"

for i in $(seq 0 5)
do
    curl -X PUT  http://localhost:8080/api/carts  -H "cart-id: 1"   -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "productCode": "c'${i}'" }'  -H "content-type: application/json"   &
    echo "done request ${i}"
done
