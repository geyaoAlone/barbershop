#项目所依赖的镜像
FROM java:8
#将maven构建好的jar添加到镜像中
ADD target/*.jar app.jar
#暴露的端口号
EXPOSE 8801
#镜像所执行的命令
ENTRYPOINT ["java","-jar","/app.jar"]

#日志支持中文
ENV LANG C.UTF-8
ENV LANGUAGE zh_CN.UTF-8
ENV LC_ALL C.UTF-82020

#时间设置为上海时间
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
