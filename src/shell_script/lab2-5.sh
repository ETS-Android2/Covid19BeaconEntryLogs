#!/bin/sh
myFunction(){
	echo "함수 안으로 들어왔음"
	eval ls $1
return
}
echo "프로그램을 시작합니다."
myFunction $1
