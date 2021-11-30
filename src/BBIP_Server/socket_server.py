import socket
from telegram.ext import Updater

#telegram_bot
my_token = "123465798:PlzWriteYourBotTokenHere"
my_chat_id = "123456798"
#Server Binding
ip = "0.0.0.0";
port = 5050;

pre_client = "";

updater = Updater(my_token, use_context=True)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

#socket host할 주소 설정
recv_address = (ip, port)
#socket 서버 구동 시작
sock.bind(recv_address)
while(True):
    #socket 서버 client 대기
    sock.listen(1)
    #들어오는 연결에 대해 승낙, connection 저장
    conn, addr = sock.accept()

    #데이터 수신
    data = conn.recv(1024)
    data = data.decode().split(";");

    if(data[0] == "out"):
        updater.bot.send_message(my_chat_id, "손님 퇴장");
        print("손님 퇴장");
        pre_client = "";
        continue;

    entry = data[0];
    client = data[1];
    menu = data[2];

    if(pre_client == client):
        continue;

    if(entry == "in") and (client != "a1"):
        updater.bot.send_message(my_chat_id, "손님 입장 - 식별자: %s 주문메뉴: %s" %(client, menu));
        print("손님 입장 - 식별자: %s 주문메뉴: %s" %(client, menu));
    elif(entry == "in") and (client == "a1"):
        updater.bot.send_message(my_chat_id, "확진자 입장 - 식별자: %s" %(client));
        print("확진자 입장 - 식별자: %s" %(client));


    pre_client = client;

    #socket 서버 종료
    conn.close()


