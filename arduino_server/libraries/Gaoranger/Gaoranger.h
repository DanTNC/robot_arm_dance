#ifndef GAORANGER_h
#define GAORANGER_h

#include <YunClient.h>
  
class STATES{
	
public:
	STATES(void);
	STATES(int _base, int _shoulder, int _elbow, int _wrist_ver, int _wrist_rot, int _gripper);
	int base;
	int shoulder;
	int elbow;
	int wrist_ver;
	int wrist_rot;
	int gripper;
	void outjson(YunClient client);
};

class RAD{
  
public:
  RAD(void);
  STATES probe(void);
  STATES do_action(int action, int param);
  
private:
  STATES states;
  
};  
  
#endif  