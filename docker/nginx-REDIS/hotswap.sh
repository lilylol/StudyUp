#!/bin/bash

#reading the PARAM
pram=$*

#getting the original IP
sed -n "/.*:6379/p" /etc/nginx/nginx.conf > check.txt

#making a string with new IP
echo "    server $pram:6379;" > base.txt

#compare two IPs
diff=$(diff check.txt base.txt)

#if IPs are the same
if [ -z "$diff" ]
then
	echo "same address, no need to refresh"
#if IPs are different
else
	#change the IP
	sed -i "s/.*:6379/    server $pram:6379/g" /etc/nginx/nginx.conf
	#reload the container
	/usr/sbin/nginx -s reload
fi

