#include "Gaoranger.h"

STATES::STATES(void){
}

STATES::STATES(int _base, int _shoulder, int _elbow, int _wrist_ver, int _wrist_rot, int _gripper){
	base = _base;
	shoulder = _shoulder;
	elbow = _elbow;
	wrist_ver = _wrist_ver;
	wrist_rot = _wrist_rot;
	gripper = _gripper;
}

void STATES::outjson(YunClient client){
	client.print("{\"base\":");
	client.print(base);
	client.print(",\"shoulder\":");
	client.print(shoulder);
	client.print(",\"elbow\":");
	client.print(elbow);
	client.print(",\"wrist\":");
	client.print(wrist_ver);
	client.print(",\"rotate\":");
	client.print(wrist_rot);
	client.print(",\"gripper\":");
	client.print(gripper);
	client.print("}");
}

RAD::RAD(void){
	reset();
}

void RAD::reset(void){
	states = *(new STATES(0, 40, 180, 0, 170, 73));
}

STATES RAD::probe(void){
	return states;
}

STATES RAD::do_action(int action, int param){
	int target;
	switch(action){
	case 0: // 0 - 180
		target = states.base + param;
		if(target < 0){
			target = 0;
		}
		if(target > 180){
			target = 180;
		}
		states.base = target;
		break;
	case 1: // 15 - 165
		target = states.shoulder + param;
		if(target < 15){
			target = 15;
		}
		if(target > 165){
			target = 165;
		}
		states.shoulder = target;
		break;
	case 2: // 0 - 180
		target = states.elbow + param;
		if(target < 0){
			target = 0;
		}
		if(target > 180){
			target = 180;
		}
		states.elbow = target;
		break;
	case 3: // 0 - 180
		target = states.wrist_ver + param;
		if(target < 0){
			target = 0;
		}
		if(target > 180){
			target = 180;
		}
		states.wrist_ver = target;
		break;
	case 4: // 0 - 180
		target = states.wrist_rot + param;
		if(target < 0){
			target = 0;
		}
		if(target > 180){
			target = 180;
		}
		states.wrist_rot = target;
		break;
	case 5: // 10 - 73
		target = states.gripper + param;
		if(target < 10){
			target = 10;
		}
		if(target > 73){
			target = 73;
		}
		states.gripper = target;
		break;
	}
	return states;
}

