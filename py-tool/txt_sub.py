## -*- coding: utf-8 -*-
#将新概念四中每个lesson进行分割，存储到单独的txt中
f = open('nce4_book.txt','r')
line = f.readline()
saveFlag = 0
buf = ''
num = ''
saveName = ''
while line:
	#获取每行数据之后，分割为独立的词
	list = line.split(' ')
	for words in list:
		#遇到'Lesson'进行处理
		if(words == 'Lesson'):
			if saveFlag == 0:
				saveFlag = 1
				saveName = line	
				print "record saveName:" + saveName
			else:
				#将buf保存到文件 文件名为saveName
				#去掉左右空格
				saveName =  saveName.strip()+'.txt'
				saveName = saveName.lower()				
				print "save file:" + saveName
				#print "buf to saved : " + buf			 
				saveFile = open(saveName,'w')
				saveFile.write(buf)
				saveFile.close()
				saveName = line
				#保存之后清空buf
				buf = ''	
	buf += line
	line = f.readline();
#最后一篇 单独处理
if buf:
	#将buf保存到文件 文件名为saveName
	#去掉左右空格
	saveName =  saveName.strip()+'.txt'	
	saveName = saveName.lower()				
	print "save file:" + saveName
	#print "buf to saved : " + buf			 
	saveFile = open(saveName,'w')
	saveFile.write(buf)
	saveFile.close()
f.close()