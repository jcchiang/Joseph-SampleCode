#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <ctime>
#include <direct.h>
#include <ctime>
#include <vector>
#include <algorithm>
#include <windows.h>
#include <iostream>
#include <string>
#include <fstream> 
#include "opencvheader.h"
using namespace std;

typedef struct FP{
	CvPoint lefteye;
	CvPoint	righteye;
	CvPoint	nose;
	CvPoint	mouth;
} FP;

typedef struct FaceInfo{
	IplImage* face;
	CvPoint leftTop;
	CvPoint	rightTop;
	CvPoint	leftdown;
	CvPoint	rightdown;
	//int Pos; //  0 左 1中 2右
    //int Gender; // 0代表女性  1代表男性
} FaceInfo;