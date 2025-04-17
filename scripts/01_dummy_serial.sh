#!/bin/zsh

#  -H "content-type: application/json"

for i in $(seq 0 3)
do
    curl http://localhost:8080/dummy/${i} -H "fe-req-id: ${i}"
    echo "done request ${i}"
done
