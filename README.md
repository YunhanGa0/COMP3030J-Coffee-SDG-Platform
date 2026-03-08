# Coffee_SDG_Platform
>This is my work for BDIC course *COMP3030J-Software Engineering Project*
>
>View our website on https://csi420-02-vm2.ucd.ie/

A Springboot and Vue based platform aiming to support sustainable development of coffee farmers
## Documentation
| Document | Description |
|----------|-------------|
| [Team Agreement](Doc/Group_1_USPA%20.pdf) | Team agreement and collaboration contract |
| [项目提案 (Project Proposal)](Doc/Group_1_USPA.pdf) | Project proposal and requirements |
| [用户文档 (User Documentation)](Doc/Group_1_user_final.pdf) | End-user guide and manual |
| [系统文档 (System Documentation)](Doc/Group_1_system_final.pdf) | System design and technical documentation |
| [交付视频 (Delivery Video)](https://www.lab.withyunhan.com/posts/course-reference-videos/) | Delivery Video for customer |

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
