sed -in 's/#LoadModule proxy_module/LoadModule proxy_module/' /usr/local/apache2/conf/httpd.conf
sed -in 's/#LoadModule proxy_http_module/LoadModule proxy_http_module/' /usr/local/apache2/conf/httpd.conf
sed -in 's/#LoadModule access_compat_module/LoadModule access_compat_module/' /usr/local/apache2/conf/httpd.conf
sed -in 's/#LoadModule ssl_module/LoadModule ssl_module/g' /usr/local/apache2/conf/httpd.conf
echo "SSLProxyEngine on" >> /usr/local/apache2/conf/httpd.conf
echo "ProxyPass /api $API_URL/api" >> /usr/local/apache2/conf/httpd.conf
echo "ProxyPassReverse /api $API_URL/api" >> /usr/local/apache2/conf/httpd.conf