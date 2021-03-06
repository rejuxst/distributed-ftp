//============================================================================
// Name        : QuorumCode.cpp
// Author      : Banji
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <jni.h>
#include "Encryption_Encrypt.h"
using namespace std;

struct info{
      char* ipAddress;
      char* username;
      char* pwd;
      char* key;
      char* serverIp;
}testInfo;

JNIEXPORT jobject JNICALL Java_Encryption_Encrypt_initialize
  (JNIEnv * env, jclass clazz)
{
	testInfo entryInfo;
	   //jclass clazz;
	   jfieldID fid;
	   //jmethodID mid;

	   clazz = (*env)->GetObjectClass(env, this);
	   if (0 == clazz)
	      {
	         printf("GetObjectClass returned 0\n");
	         return(-1);
	      }

	   fid = (*env)->GetFieldID(env, this, "ipAddress", "Ljava/lang/String;");
	   (*env)->SetObjectField(env,this,fid,testInfo.ipAddress);

	   fid = (*env)->GetFieldID(env, this, "username", "Ljava/lang/String;");
	   (*env)->SetObjectField(env,this,fid,testInfo.username);

	   fid = (*env)->GetFieldID(env, this, "pwd", "Ljava/lang/String;");
	   (*env)->SetObjectField(env,this,fid,testInfo.pwd);

	   fid = (*env)->GetFieldID(env, this, "serverIp", "Ljava/lang/String;");
	   (*env)->SetObjectField(env,this,fid,testInfo.serverIp);


	   //GetInfo(entryInfo);   // fills in the entryInfo
}

int main() {

	cout << "!!!Hello World!!!" << endl; // prints !!!Hello World!!!
	return 0;
}
