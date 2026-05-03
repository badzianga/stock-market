#!/bin/bash

docker rm -f nginx app1 app2 app3 postgres
docker network rm stock-market-net 2>/dev/null
