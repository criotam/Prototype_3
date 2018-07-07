# -*- coding: utf-8 -*-
"""
Created on Fri Jul  6 11:12:27 2018

@author: Anuj
"""

import websocket
import _thread
import time
import sys


message = sys.argv[1]
print(message)

def on_message(ws, message):
    print(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print ("### closed ###")

def on_open(ws):
    print ("connection opened")
    ws.send(message)
    ws.close()

if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp("ws://192.168.1.4:8080/WebServer/exp2loadcelllistener",
                              on_message = on_message,
                              on_error = on_error,
                              on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()