### 同时启动shell脚本  
```shell
#!/bin/bash

array=($(ls /root/atguigucrowd | grep .jar))

len=${#array[@]}

case $1 in
"start"){
	for((i=0; i<$len; i++))
	do
	one=${array[$i]}
	nohup /usr/lib/jvm/java-8-openjdk-amd64/bin/java -jar $one 1>>./boot.log 2>&1 & 
	done
};;
"stop"){

		#空缺待补		
		echo "空缺待补"						        
		};; 

esac
```