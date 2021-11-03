echo "리눅스가 재미있나요? (yes / no)"
read test
#echo $test

case $test in
	"yes" | "Yes" | "YEs" | "YES" | "y" | "Y" | "yesyes" | "YESYES")
		echo "yes";;
	"no" | "nono" | "nonono" | "N" | "n" | "NO" | "NONO" | "NONONO" )
		echo "no";;
	*)
		echo "yes or no로 입력해 주세요";;
esac
