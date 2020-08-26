# kim
A chatroom with Private Protocol based on netty.

更多说明及实现请查看[博客](https://rynuk.xyz/kimi-netty.html)

## 说明

请先打包成`jar`包后运行或者

使用IDE运行的时候，请先运行服务端后再启动客户端

## 特性

- 使用netty的线程池，具备多线程高效率的特性

- 具备CLI命令行脚手架

  - help帮助
  - login登录
  - listUsers列出在线用户
  - send发送消息
  - 未知命令异常处理
  - 异常提示
  
- 基于Netty私有协议开发，具备心跳检测和客户端失败重连机制（该部分参考《Netty权威指南2.0》一书）

- 具备对象解码器

- 支持一对一私聊和一对多群聊

- 具备UI界面（使用SceneBuilder编写，目前只实现了加入群聊、发送消息和退出功能）

## 部分运行效果

![image](src/images/send.png?raw=true)

![image](src/images/send121.png?raw=true)

![image](src/images/send2all.png?raw=true)