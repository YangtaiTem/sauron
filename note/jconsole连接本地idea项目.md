### JConsole 连接本地idea解决方法，亲测可用

0.问题环境
    
    jdk:1.8.0_77
    IDEA:2018.3.3

1.解决的问题
    
    JConsole 连接本地idea,查看idea中的项目运行情况。
        一开始选择的是连接本地进程，但是失败，一直提示：
            连接失败：是否重试？未能成功连接到巴拉巴拉，这种方法失败···   
        然后网上查找解决办法，找到另外一种可用的方法：
            1.在idea界面找到你启动项目的命令按钮，下拉选择Edit Configurations选项；
            2.在启动项目的命令的VM options中，加入以下内容：
               -Dcom.sun.management.jmxremote 
               -Dcom.sun.management.jmxremote.port=8099
               -Dcom.sun.management.jmxremote.ssl=false  
               -Dcom.sun.management.jmxremote.authenticate=false
            ps:有些情况下，VM options一开始找不到，需要点击页面上的Enviroment,展开后看到这个参数。
            3.打开Jconsole，选择连接远程进程 localhost:8099,可以正常连接上。       