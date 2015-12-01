// PR 2011 Fall

#include "faceDefine.h"

// include toolkit class
#include "FaceDetection.h"
#include "OpenCVASMAlignment.h"
#include "FaceCrop.h"

// include our class
#include "FaceEqualCorrelizer.h"
#include "DifferenceOfGaussian.h"

// include for models
#include "opencv2/core/core.hpp"
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <time.h>
#include <iostream>
#include <fstream>
#include <sstream>

using namespace cv;
using namespace std;

FaceEqualCorrelizer *face_ECizer;
FILE *output;

FaceDetection *face_detector;
OpenCVASMAlignment *myAlignment;
FaceCrop *FaceCropper;
vector<FaceInfo> CropFaceSet;   
vector<IplImage*> FaceSet;
vector<FP> FaceFP;
char tmp[100];
char folder[100];
char studentID[100];
char type[100];
char folderwithout[100];
char filename[150];
char dir[300];
char resultdir[200];
char savingfolder[200];

char *inputtoken;
char *inputfilename;
char id[54][20];
char input[300], tokens[20][40];
	

void LoadIDTable(){
	char idfile[300] = "id_label.txt";
	FILE *idload;

	idload = fopen(idfile, "r");
	for(int i = 0; i < 54; i++){
		fscanf(idload, "%s", id[i]);
	}
}

void GetInputFileName(){
	char *token;
	int count = 0;
	strcpy(input ,dir);
	token = strtok(input, "/\.");
	while(token != NULL){
		strcpy(tokens[count], token);
		count++;
		//cout<<token<<","<<token1<<","<<token2<<endl;
		token = strtok(NULL, "/\.");
	}
	inputtoken = tokens[count-2];
	cout<<"INPUT:"<<inputtoken<<endl;
	strcpy(input, tokens[count-2]);
	strcat(input, ".");
	strcat(input, tokens[count-1]);
	inputfilename = input;
	cout<<"INPUT FILE NAME:"<<inputfilename<<endl;
	
}

void FaceCropping(IplImage *img){
	        IplImage *ProcessImage = cvCloneImage(img);
			CropFaceSet.clear();
			FaceSet.clear(); 
			
			FaceCropper = new FaceCrop();
			FaceCropper->Initial(ProcessImage, FaceFP);
			FaceCropper->setParameter("CropConfig.txt");
			//FaceCropper ->setParameter(0.9,1.5,2.0,80,100,5.0);
            //FaceCropper->CropAllGrayLevelFace(&CropFaceSet);
			FaceCropper->CropAllFace(&CropFaceSet, &FaceSet );
			cvReleaseImage(&ProcessImage);
            delete FaceCropper;
			
}


void main()
{
	vector<CvRect> faceWindow; //store each face's rect position
	vector<CvMat*> alignmentResult; // stroe facial points

	face_detector = new FaceDetection("cascade Data\\haarcascade.xml");
    myAlignment = new OpenCVASMAlignment();
	myAlignment->setModelPath("ASM Data/FrontalFace_best.amf");
	if (myAlignment->loadModel() == -1) {
		fprintf(stderr,"Could not load ASM model...\n");
		return;
	}
    


	IplImage *img;
	IplImage *drawimg;

	cout << "Please input your folder: ";
	cin >> tmp;
	cout << "Enter your student ID (ex:d98922030):";
	cin >> studentID;
	sprintf(savingfolder,"./%s_%s",tmp, studentID); 
  	mkdir(savingfolder);

	//
	face_ECizer = new FaceEqualCorrelizer();
	output = fopen("at-gamma.txt", "w");

	FILE *result;
	char result_path[100];
	sprintf(result_path, "%s/RecResult.txt", savingfolder);
	result = fopen(result_path, "w");

	LoadIDTable();
	//

	sprintf(folder, "%s/*.*", tmp);
	sprintf(folderwithout, "%s/", tmp);
		
	size_t origsize = strlen(folder) + 1;
    const size_t newsize = 100;
    size_t convertedChars = 0;
    wchar_t wcstring[newsize];
    mbstowcs_s(&convertedChars, wcstring, origsize, folder, _TRUNCATE);

    WIN32_FIND_DATA FindFileData;
    HANDLE FileHandle;
	FileHandle = FindFirstFile(wcstring, &FindFileData);
    WideCharToMultiByte( CP_ACP, 0, FindFileData.cFileName, -1,filename, sizeof(filename), NULL, NULL ); 

	// Load Model
	Ptr<FaceRecognizer> model_fisher_gamma = createFisherFaceRecognizer();
	model_fisher_gamma->load("./Model/Fisher_gamma.yml");
	Ptr<FaceRecognizer> model_fisher_dog = createFisherFaceRecognizer();
	model_fisher_dog->load("Model/Fisher_dog.yml");
	Ptr<FaceRecognizer> model_fisher_original = createFisherFaceRecognizer();
	model_fisher_original->load("Model/Fisher_original.yml");

	Ptr<FaceRecognizer> model_eigen_gamma = createEigenFaceRecognizer();
	model_eigen_gamma->load("Model/EigenFace_gamma.yml");
	Ptr<FaceRecognizer> model_eigen_dog = createEigenFaceRecognizer();
	model_eigen_dog->load("Model/EigenFace_dog.yml");
	Ptr<FaceRecognizer> model_eigen_original = createEigenFaceRecognizer();
	model_eigen_original->load("Model/EigenFace_original.yml");

	Ptr<FaceRecognizer> model_lbp_gamma = createLBPHFaceRecognizer();
	model_lbp_gamma->load("Model/LBPHFace_gamma.yml");
	Ptr<FaceRecognizer> model_lbp_dog = createLBPHFaceRecognizer();
	model_lbp_dog->load("Model/LBPHFace_dog.yml");
	Ptr<FaceRecognizer> model_lbp_original = createLBPHFaceRecognizer();
	model_lbp_original->load("Model/LBPHFace_original.yml");
	//Here is how to predict
	/*
	Mat image = imread("./REC_b98902081_dog/REC_B97203034_1_dog.tiff", 0);
	int predict_label = -1;
	double confidence = -1;
	model->predict(image, predict_label, confidence);
	*/

	//

	while(GetLastError()!=ERROR_NO_MORE_FILES){	
	   		   sprintf(dir,"%s/%s", folderwithout, filename);
			   
			   cout<< "FOLDER: " << folder << endl;
			   cout<< "FOLDER_WITHOUT: " << folderwithout << endl;
			   
			   if(  (img = cvLoadImage(dir)) ==0 )// Load img
					   printf("%s cannot open!\n", dir);			
				else{
					        drawimg = cvCloneImage(img);
							GetInputFileName();
							cout<< "dir: " << dir << endl;
 					        faceWindow = face_detector->Detect(img);
	                        if(faceWindow.size() != 0)
							{
								 for(int i=0; i<faceWindow.size(); i++)
									 cvRectangle(drawimg,cvPoint(faceWindow[i].x, faceWindow[i].y),cvPoint(faceWindow[i].x+faceWindow[i].width, faceWindow[i].y+faceWindow[i].height),CV_RGB(255,0,0));
								 myAlignment->SetImage(img);
								 for(int i=0; i<faceWindow.size(); i++)	
									 alignmentResult.push_back(myAlignment->calcAlignment(faceWindow[i]));
								 for(int i=0; i<alignmentResult.size(); i++){
								   for (int k = 0; k < alignmentResult[i]->rows; k++)
									 if(k == 31 ||k == 36 || k == 66 || k == 67)
										cvCircle(drawimg, cvPoint(cvmGet(alignmentResult[i], k, 0), cvmGet(alignmentResult[i], k, 1)), 1, CV_RGB(255, 0, 0), 2);
									 else
										cvCircle(drawimg, cvPoint(cvmGet(alignmentResult[i], k, 0), cvmGet(alignmentResult[i], k, 1)), 1, CV_RGB(0, 255, 0), 2);
								     
									 FP AlignOK;
						             AlignOK.righteye.x = cvmGet(alignmentResult[i], 36, 0);
						             AlignOK.righteye.y = cvmGet(alignmentResult[i], 36, 1);
						             AlignOK.lefteye.x = cvmGet(alignmentResult[i], 31, 0);
						             AlignOK.lefteye.y = cvmGet(alignmentResult[i], 31, 1);;
						             AlignOK.mouth.x = cvmGet(alignmentResult[i], 66, 0);
						             AlignOK.mouth.y = cvmGet(alignmentResult[i], 66, 1);
						             AlignOK.nose.x = cvmGet(alignmentResult[i], 67, 0);
						             AlignOK.nose.y = cvmGet(alignmentResult[i], 67, 1);
						             FaceFP.push_back(AlignOK);
								 }
							}
							FaceCropping(img);//¤HÁyµô¤Á
							IplImage *gammaDst;
							IplImage* dogSrc, *dogDst;
							for(int i=0; i<CropFaceSet.size()  ;i++){
								cvLine(drawimg,CropFaceSet[i].leftdown,CropFaceSet[i].leftTop,CV_RGB(255,255,0),2);
								cvLine(drawimg,CropFaceSet[i].leftTop,CropFaceSet[i].rightTop,CV_RGB(255,255,0),2);
								cvLine(drawimg,CropFaceSet[i].rightTop,CropFaceSet[i].rightdown,CV_RGB(255,255,0),2);
								cvLine(drawimg,CropFaceSet[i].rightdown,CropFaceSet[i].leftdown,CV_RGB(255,255,0),2);
								
								//cvShowImage("A", FaceSet[i]);
								//FaceEqualAndGamma(i, 0.8);
								
								/* Equalize and Gamma Correlation */
								face_ECizer->Equalizer(FaceSet[i], &gammaDst);
								/* Difference Of Gaussian*/
								dogSrc = cvCloneImage(gammaDst);
								dogDst = cvCreateImage(cvGetSize(dogSrc), dogSrc->depth, dogSrc->nChannels);
								int kernelSize = 7, sigma1 = 3, sigma2 = 1;
								DifferenceOfGaussian dogOperator(kernelSize, sigma1, sigma2);
								dogOperator.output(dogSrc, dogDst);

								//cvShowImage("B", FaceSet[i]);
								//cvShowImage("Difference Of Gaussian",dogDst);
								
								//IplImage to Mat
								cv::Mat mat_original(FaceSet[i], 0);
								cv::Mat mat_gamma(gammaDst, 0);
								cv::Mat mat_dog(dogDst, 0);

								// Prediciton
								int predict_label = -1;
								int output_label[9]={0};
								double confidence[9] = {0};
								model_fisher_gamma->predict(mat_gamma, output_label[0], confidence[0]);
								model_fisher_dog->predict(mat_dog, output_label[1], confidence[1]);
								model_fisher_original->predict(mat_original, output_label[2], confidence[2]);
								model_eigen_gamma->predict(mat_gamma, output_label[3], confidence[3]);
								model_eigen_dog->predict(mat_dog, output_label[4], confidence[4]);
								model_eigen_original->predict(mat_original, output_label[5], confidence[5]);
								model_lbp_gamma->predict(mat_gamma, output_label[6], confidence[6]);
								model_lbp_dog->predict(mat_dog, output_label[7], confidence[7]);
								model_lbp_original->predict(mat_original, output_label[8], confidence[8]);

								double min_confidence[3] = {999999,999999,999999};
								int min_confidence_idx[3] = {-1};
								for(int counter = 0 ; counter < 9 ; counter++){
									if(confidence[counter] < min_confidence[0] || confidence[counter] < min_confidence[1] || confidence[counter] < min_confidence[2]){
										if(min_confidence[0] > min_confidence[1] && min_confidence[0] > min_confidence[2]){
											min_confidence[0] = confidence[counter];
											min_confidence_idx[0] = output_label[counter];
										}
										else if(min_confidence[1] > min_confidence[0] && min_confidence[1] > min_confidence[2]){
											min_confidence[1] = confidence[counter];
											min_confidence_idx[1] = output_label[counter];
										}
										else if(min_confidence[2] > min_confidence[0] && min_confidence[2] > min_confidence[1]){
											min_confidence[2] = confidence[counter];
											min_confidence_idx[2] = output_label[counter];
										}
										else if(min_confidence[0] == min_confidence[1] && min_confidence[0] > min_confidence[2]){
											min_confidence[0] = confidence[counter];
											min_confidence_idx[0] = output_label[counter];
										}
										else if(min_confidence[1] > min_confidence[0] && min_confidence[1] == min_confidence[2]){
											min_confidence[1] = confidence[counter];
											min_confidence_idx[1] = output_label[counter];
										}
										else if(min_confidence[2] == min_confidence[0] && min_confidence[2] > min_confidence[1]){
											min_confidence[0] = confidence[counter];
											min_confidence_idx[0] = output_label[counter];
										}
										else if(min_confidence[2] == min_confidence[0] && min_confidence[2] == min_confidence[1]){
											min_confidence[0] = confidence[counter];
											min_confidence_idx[0] = output_label[counter];
										}
										else{
											cout << "WTF" << endl;
										}
									}
									//cout << "output_label[" << counter << "] = " << output_label[counter] << endl;
									//cout << "confidence[" << counter << "] = " << confidence[counter] << endl;
								}
								for(int counter = 0 ; counter < 3 ; counter++){
									cout << "min_confidence[" << counter << "] = " << min_confidence[counter] << endl;
									cout << "min_confidence_idx[" << counter << "] = " << min_confidence_idx[counter] << endl;
								}
								if((min_confidence_idx[0] == min_confidence_idx[1] || min_confidence_idx[0] == min_confidence_idx[2]) && min_confidence_idx[0] != 999999){
									cout << "Predict label : " << min_confidence_idx[0] <<endl;
									predict_label = min_confidence_idx[0];
								}
								else if(min_confidence_idx[1] == min_confidence_idx[2] && min_confidence_idx[1] != 999999){
									cout << "Predict label : " << min_confidence_idx[1] <<endl;
									predict_label = min_confidence_idx[1];
								}
								else{
									int minimum_idx = -1;
									double minimum_conf = 999999;
									for(int counter = 0 ; counter < 3 ; counter++){
										if(min_confidence[counter] < minimum_conf){
											minimum_conf = min_confidence[counter];
											minimum_idx = counter;
										}
									}
									cout << "Predict label : " << min_confidence_idx[minimum_idx] <<endl;
									predict_label = min_confidence_idx[minimum_idx];
								}

								char *node = strrchr(filename, '.');
			                    if(*node != NULL) *node ='\0';
								//sprintf(resultdir,"%s/%s_dog.tiff", savingfolder, filename);
								cout<<savingfolder<<endl;
								sprintf(resultdir,"%s/Result_%s_%s.jpg", savingfolder, inputtoken, id[predict_label]); 

								cvSaveImage( resultdir, FaceSet[i]);
								//cvSaveImage( resultdir, dogDst);

								//printf("%s", resultdir);
								fprintf(output, "%s;\n", resultdir);

								fprintf(result, "%s %s\n", inputfilename, id[predict_label]);
								//csv<<inputfilename<<" "<<id[predict_label]<<endl;

							 }

							//cvNamedWindow("ivFace");
							//cvShowImage("ivFace",drawimg);
							//cvWaitKey(0);
							alignmentResult.clear();	
							faceWindow.clear();
							FaceFP.clear();	
							cvReleaseImage(&img);
			                cvReleaseImage(&drawimg);		
			   }
			   
			   FindNextFile(FileHandle,&FindFileData);
			   WideCharToMultiByte( CP_ACP, 0, FindFileData.cFileName, -1,filename, sizeof(filename), NULL, NULL );
	  }

	fclose(output);
	fclose(result);
	//csv.close();
}

