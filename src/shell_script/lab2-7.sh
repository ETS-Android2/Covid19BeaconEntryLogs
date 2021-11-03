#!/bin/bash

# 디렉토리 존재 유무 확인
if [ ! -d $1 ]; then
	mkdir $1
fi
temp=$1
temp2=`expr length "${temp}" - 1`
name=`echo $temp | cut -c 1-$temp2`

for i in `seq 1 5`
do
	j=`expr $i - 1`
	touch $1/$name$j".txt"
	mkdir $1/$name$j
	eval ln -s $1/$name$j".txt" $1/$name$j/$name$j".txt"
done
