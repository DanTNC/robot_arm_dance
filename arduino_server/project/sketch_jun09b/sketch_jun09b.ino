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
    probe_node(client, query);
    return;
  }

  if(node == "action"){
    action_node(client, query);
    return;
  }

  if(node == "choreography"){
    choreo_node(client, query);
    return;
  }

  if(node == "toward"){
    toward_node(client, query);
    return;
  }
}

void probe_node(YunClient client, String query){
  //probe/states
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
}

void action_node(YunClient client, String query){
  //action/<motor>/<angle>
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
}

void choreo_node(YunClient client, String query){
  //choreography/<numOfActions>/<type>:<angle>;<type>:<angle>;...
  int numOfActions = nextNode(query).toInt();
  String actions[numOfActions];
  StringSplit(query, ";", actions, numOfActions);
  for (int i = 0; i < numOfActions; i++){
    String action = actions[i];
    String motor = nextDelim(action, ":");
    String param = action;
    STATES states = rad.do_action(motor.toInt(), param.toInt());
    moveMotors(states);
    //states.outjson(client);
  }
  client.print("{\"result\":\"ok\"}");
}

void toward_node(YunClient client, String query){
  //toward/<base_ang>;<shoulder_ang>;<elbow_ang>;<wrist_ang>;<rotate_ang>;<gripper_ang>;
  String angles[6];
  StringSplit(query, ";", angles, 6);
  STATES states = rad.set_states(*(new STATES(angles[0].toInt(), angles[1].toInt(), angles[2].toInt(), angles[3].toInt(), angles[4].toInt(), angles[5].toInt())));
  moveMotors(states);
  client.print("{\"result\":\"ok\"}");
}

void StringSplit(String target, String delim, String container[], int len){
  for (int i = 0; i < len; i++){
    String action = nextDelim(target, delim);
    container[i] = action;
  }
}

void writeHeader(YunClient client){
   client.println("Status: 200");
   client.println("Content-type: application/json");
   client.println();
}

void moveMotors(STATES states){
   Braccio.ServoMovement(0, states.base, states.shoulder, states.elbow, states.wrist_ver, states.wrist_rot, states.gripper);
}

String nextDelim(String &query, String delim){
   int nextSlash = query.indexOf(delim);
   String res;
   if(nextSlash == -1){
     res = "$$";
   }else{
     res = query.substring(0, nextSlash);
     query = query.substring(nextSlash + 1);
   }
   return res;
}

String nextNode(String& query){
   return nextDelim(query, "/");
}
