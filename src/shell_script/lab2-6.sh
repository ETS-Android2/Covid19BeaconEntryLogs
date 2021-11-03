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
	touch $1/$name`expr $i - 1`".txt"
done


tar -cvf $1/$1".tar" $1


tar -xf $1/$1".tar" -C $1

mv $1/$1".tar" $1/$1/$1".tar"
