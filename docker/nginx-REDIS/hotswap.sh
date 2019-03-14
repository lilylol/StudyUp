#!/bin/bash

#reading the PARAM
pram=$*

#reading the SHA for the nginx_container
#sha=$(docker ps --filter "name=docker_database_1" --format "{{.ID}}")

#entering the running nginx_container
#docker exec -it "$sha" bash

#getting the original IP
sed -n "/.*:6379/p" /etc/nginx/nginx.conf > check.txt

#making a string with new IP
echo "    server $pram:6379;" > base.txt

#compare two IPs
diff=$(diff check.txt base.txt)

#if IPs are different
if ("$diff" != "")
{
	#change the IP
	sed -i "s/.*:6379/    server $pram:6379/g" /etc/nginx/nginx.conf
	#reload the container
	/usr/sbin/nginx -s reload
}
#IPs are the same
else
{
	echo "same address, no need to refresh"
}

