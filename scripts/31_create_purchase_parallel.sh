#!/bin/zsh

#  -H "content-type: application/json"

for i in $(seq 0 100)
do
      curl -X POST http://localhost:8080/api/purchases -H "fe-req-id: ${i}"  -d '{ "cartId": 1, "cartVersion": 3  }'  -H "content-type: application/json"   &
      echo "done request ${i}"
done
