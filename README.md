#### Simple Http image sharing service ####
**Please note, this is for use only in protected network**   

Only two methods.  
*Download file* 

```bash
curl -X GET \
  http://localhost:8080/image/12.jpg \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'cache-control: no-cache'
```

*Upload file*

```bash
curl -X POST \
  http://localhost:8080/image \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 1447407' \
  -H 'Content-Type: multipart/form-data; boundary=--------------------------303989634438073582793471' \
  -H 'Host: localhost:8080' \
  -H 'cache-control: no-cache' \
  -F file=@/C:/picture.jpeg
```

*Start server*

```
java -jar image-server-1.0-SNAPSHOT.jar
```

Application starts on 8080 port by default 
The application creates a directory at the jar level. 
Directory name is set in the application.properties and by default it 'shared'.
The downloaded files will be added to this directory

*Docker*

Ready docker image 

```
docker run -d -v {path to shared folder on host}:/usr/src/myapp/shared -p 8080:8080 dmtry/image-sharing-service
```







