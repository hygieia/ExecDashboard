#!/bin/bash

sed -in 's/#LoadModule proxy_module/LoadModule proxy_module/' /usr/local/apache2/conf/httpd.conf
sed -in 's/#LoadModule proxy_http_module/LoadModule proxy_http_module/' /usr/local/apache2/conf/httpd.conf
sed -in 's/#LoadModule ssl_module/LoadModule ssl_module/g' /usr/local/apache2/conf/httpd.conf

echo "SSLProxyEngine on" >> /usr/local/apache2/conf/httpd.conf
echo "ProxyPass /api $API_URL/api retry=0" >> /usr/local/apache2/conf/httpd.conf

exec "$@"