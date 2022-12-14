# Nginx-RTMP

Linux操作：

下载nginx

​	wget http://nginx.org/download/nginx-1.15.3.tar.gz

解压

​	tar xvf nginx-1.15.3.tar.gz

下载nginx rtmp模块

​	wget https://codeload.github.com/arut/nginx-rtmp-module/tar.gz/v1.2.1

解压

​	tar xvf v1.2.1  

进入nginx目录

​	cd nginx-1.15.3

执行：

```shell
#--add-module 指向rtmp模块目录
./configure --prefix=./bin --add-module=../nginx-rtmp-module-1.2.1
```



在这个过程中可能因为环境不同而出现不同错误，比如缺少pcre、openssl等，这时候就需要安装这些库。

https://blog.csdn.net/z920954494/article/details/52132125



编译完成后，安装在当前目录的bin目录下。

cd bin/conf 

vim nginx.conf 修改为：

```nginx
user root;
worker_processes  1;

error_log  logs/error.log debug;

events {
    worker_connections  1024;
}

# rtmp://ip:port/myapp
rtmp {
    server {
         #注意端口占用，rtmp端口
        listen 1935;
        application myapp {
            live on;
            #丢弃闲置5s的连接
            drop_idle_publisher 5s;
        }
    }
}
http {
    server {
        #注意端口占用
        listen      8081;
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /stat.xsl {
            #注意目录
            root /root/ffmpeg/nginx/nginx-rtmp-module-1.2.1/;
        }
        location /control {
            rtmp_control all;
        }
        location /rtmp-publisher {
            #注意目录
            root /root/ffmpeg/nginx/nginx-rtmp-module-1.2.1/test;
        }
        
        location / {
            #注意目录
            root /root/ffmpeg/nginx/nginx-rtmp-module-1.2.1/test/www;
        }
    }
}
```

其实就是从 ` nginx-rtmp-module-1.2.1/test/nginx.conf `中拷贝。

端口占用检查：  lsof -i:8080

需要注意的是目录与端口是否被占用，比如我的8080端口被占用，我改为了8081，然后需要开放端口。

配置了iptables防火墙的翻下前面的资料，如果没安装的阿里云服务器可以进入阿里云控制台开放![阿里云控制台](imgs\阿里云控制台.png)

然后点击`配置规则`,在新页面点击添加`安全组规则`,开放8081端口，然后确定，就可以了。

![开放端口](imgs\开放端口.png)



配置完成后，就可以启动nginx了

在当前目录 执行  bin/sbin/nginx 即可启动

 bin/sbin/nginx -s  stop 停止



一定要在当前目录启动，因为上面的配置 error_log  logs/error.log debug;  会去执行命令的目录下查找 logs。

如果error_log  改成一个绝对路径 那就没关系了。





在浏览器输入 

【IP】:8081

能访问就表示配置完成了。

显示推流的设备信息等：http://ip:8081/stat

推流软件：EV录屏、OBS

