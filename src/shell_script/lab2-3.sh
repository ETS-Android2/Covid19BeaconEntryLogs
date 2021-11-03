#!/bin/bash

bmi=$(awk -v weight=$1 -v height=$2 'BEGIN {printf (weight/(height/100*height/100))}')

underweight=$(echo "$bmi<=18.5" | bc)
normal=$(echo "$bmi<=23" | bc)

if [ $underweight -eq 1 ]
then 
	echo "저체중입니다."
elif [ $normal -eq 1 ]
then 
	echo "정상체중입니다."
else
	echo "과체중입니다."
fi
