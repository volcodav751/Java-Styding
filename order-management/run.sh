#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mvn -q clean package
exec java -Dfile.encoding=UTF-8 --module-path "order-threads/target/order-threads-1.0.0.jar:order-threads/target/lib" -m order.threads/com.example.order.threads.Main
