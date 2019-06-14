#include <Bridge.h>
#include <YunServer.h>
#include <YunClient.h>
#include <Braccio.h>
#include <Servo.h>
#include "Gaoranger.h"

int LEDPIN = LED_BUILTIN; // your LED PIN
YunServer server;
RAD rad;
Servo base;
Servo shoulder;
Servo elbow;
Servo wrist_rot;
Servo wrist_ver;
Servo gripper;

void setup() {
  // Start our connection
  Braccio.begin();
  Serial.begin(9600);
  pinMode(LEDPIN,OUTPUT);
  digitalWrite(LEDPIN,HIGH); // turn on Led while connecting
  Bridge.begin();

  // Show a fancy flash pattern once connected
  digitalWrite(LEDPIN,LOW); 
  delay(150);
  digitalWrite(LEDPIN,HIGH); 
  delay(150);
  digitalWrite(LEDPIN,LOW); 
  delay(150);
  digitalWrite(LEDPIN,HIGH); 
  delay(150);
  digitalWrite(LEDPIN,LOW); 
  delay(150);
  
  // Disable for some connections:
  // Start listening for connections  
  
  // server.listenOnLocalhost();
  server.begin();
 
}

void loop() {
  // Listen for clients
  YunClient client = server.accept();
  // Client exists?
  if (client) {
    // Lets process the request!
    process(client);
    client.stop();
  }
  delay(50);
}

void process(YunClient client) {
  writeHeader(client);
  // Collect user commands
  String query = client.readStringUntil('\\'); // load whole string
  query.trim();
  String node = nextNode(query);
  if(node == "$$"){
    client.print("{\"result\":\"error\",\"message\":\"missing paramters\"}");
    return;
  }
  
  if(node == "probe"){
    String check = nextNode(query);
    if(check == "$$"){
      String part = query;
      if(part == "states"){
        rad.probe().outjson(client);
      }else{
        client.print("{\"result\":\"error\",\"message\":\"no such part\"}");
      }
    }else{
      client.print("{\"result\":\"error\",\"message\":\"too many nodes\"}");
    }
    return;
  }

  if(node == "action"){
    String action = nextNode(query);
    if(action != "$$"){
      String params = query;
      STATES states;
      if(action == "base"){
        states = rad.do_action(0, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "shoulder"){
        states = rad.do_action(1, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "elbow"){
        states = rad.do_action(2, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "wrist"){
        states = rad.do_action(3, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "rotate"){
        states = rad.do_action(4, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "gripper"){
        states = rad.do_action(5, params.toInt());
        moveMotors(states);
        client.print("{\"result\":\"ok\"}");
        return;
      }
      if(action == "led"){
        if(query == "on"){
          digitalWrite(LEDPIN, HIGH);
          client.print("{\"result\":\"ok\"}");
          return;
        }
        if(query == "off"){
          digitalWrite(LEDPIN, LOW);
          client.print("{\"result\":\"ok\"}");
          return;
        }
        if(query == "toggle"){
          int led_mode = digitalRead(LEDPIN);
          if(led_mode == HIGH){
            digitalWrite(LEDPIN, LOW);
          }else{
            digitalWrite(LEDPIN, HIGH);
          }
          client.print("{\"result\":\"ok\"}");
        }
        client.print("{\"result\":\"error\",\"message\":\"illegal paramter for led\"}");
        return;
      }
      client.print("{\"result\":\"error\",\"message\":\"no such action\"}");
    }else if(query == "reset"){
      STATES states = rad.reset();
      moveMotors(states);
      states.outjson(client);
    }else{
      client.print("{\"result\":\"error\",\"message\":\"missing action parameters\"}");
    }
    return;
  }
}

void writeHeader(YunClient client){
   client.println("Status: 200");
   client.println("Content-type: application/json");
   client.println();
}

void moveMotors(STATES states){
   Braccio.ServoMovement(20, states.base, states.shoulder, states.elbow, states.wrist_ver, states.wrist_rot, states.gripper);
}

String nextNode(String& query){
   int nextSlash = query.indexOf("/");
   String res;
   if(nextSlash == -1){
     res = "$$";
   }else{
     res = query.substring(0, nextSlash);
     query = query.substring(nextSlash + 1);
   }
   return res;
}
