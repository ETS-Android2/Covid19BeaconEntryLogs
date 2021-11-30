#!/bin/bash

FILE="/server/socket_server.py"

if [ ! -e $FILE ]; then
  cp /original/socket_server.py /server
fi

python /server/socket_server.py
