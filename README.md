# Coffee_SDG_Platform
A Springboot and Vue based platform aiming to support sustainable development of coffee farmers
## Quick start
Modify `application.properties` to your own configuration, especially the DB part
```
spring.datasource.url=YOUR_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PW
```
As a reminder, this project use `aliyun` to store images and `Openai` as AI support, you should change them to your own service or delete related code to use these functionalities.
```
aliyun.oss.endpoint=YOUR_ENDPOINT
aliyun.oss.bucketName=YOUR_NAME
aliyun.oss.baseUrl=YOUR_URL

openai.api-key=YOUR_KEY
openai.api-url=YOUR_URL

aliyun.oss.accessKeyId=YOUR_Id
aliyun.oss.accessKeySecret=YOUR_SECRET
```
