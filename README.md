# BBIP
> 블루투스 비콘 기반 전자출입명부 시스템입니다.


## 설치 방법

### BBIP_Server
BBIP_Server install:
```sh
docker run -d -p 5050:5050 -v YourComputerFolder:/server --name YourOwnName luna1474/bbip_server
```

**server 폴더 내부에 socket_server.py - telegram_bot 정보를 업데이트하시기 바랍니다.**

참고 사항 - [Telegram Bots](https://core.telegram.org/bots)

### Android Application

Beacon_Transmitter.apk:
> 가게 단말기 설치용

![image](https://user-images.githubusercontent.com/24191595/144058444-d0c57947-b830-432d-9cff-4d2d1031383c.png)




Beacon_receiver.apk:
> 손님 단말기 설치용

![image](https://user-images.githubusercontent.com/24191595/144058339-57c7913f-229c-4de7-8e14-36b3c8dc087a.png)


## 사용 예제

![BBIP_1](https://user-images.githubusercontent.com/24191595/144059605-6772d58a-cddb-4ed3-8f23-288403936ce0.gif)

## 업데이트 내역
* 1.0
    * 초기버전 어플 출시

## 정보

유휘준 – peulling@soongsil.ac.kr

박건우 - tkddlfehd111@soongsil.ac.kr

GPL 3.0 라이센스를 준수하며 ``LICENSE``에서 자세한 정보를 확인할 수 있습니다.

오픈소스기초설계(나) - 김성흠 교수님

[https://canvas.ssu.ac.kr/courses/7760](https://canvas.ssu.ac.kr/courses/7760)
