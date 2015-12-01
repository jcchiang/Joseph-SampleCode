#include "FaceEqualCorrelizer.h"
#include <opencv2/imgproc/imgproc.hpp>
using namespace cv;

void FaceEqualCorrelizer::Equalizer(IplImage* srcImg, IplImage** dstImg){
	/*
	tempImg = cvCreateImage(cvSize(srcImg->width, srcImg->height), IPL_DEPTH_8U, 1);
	eqlImg  = cvCreateImage(cvSize(srcImg->width, srcImg->height), IPL_DEPTH_8U, 1);
	cvCvtColor( srcImg, tempImg, CV_RGB2GRAY );
	cvEqualizeHist(tempImg, eqlImg);
	dstImg = cvCreateImage(cvSize(srcImg->width, srcImg->height), IPL_DEPTH_8U, 1);
	dstImg = cvCloneImage(eqlImg);
	cvReleaseImage(&tempImg);
	cvReleaseImage(&eqlImg);
	*/
	IplImage *src, *equal, *tmp;
	float fGamma = 0.7;
	//Equalized
	src = cvCloneImage(srcImg);
	equal = cvCloneImage(src);
	cvEqualizeHist(src, equal);
	//Gamma Correlation
	tmp = cvCloneImage(equal);
	unsigned char lut[256];  
    for( int i = 0; i < 256; i++ )  
    {  
        lut[i] = pow((float)(i/255.0), fGamma) * 255.0;  
    }

	cv::Mat tempMat(tmp, 0);
	const int channels = tempMat.channels();

	cv::MatIterator_<uchar> it, end;  
    for( it = tempMat.begin<uchar>(), end = tempMat.end<uchar>(); it != end; it++ )  
		*it = lut[(*it)];

	IplImage gamma= IplImage(tempMat);
	*dstImg = cvCloneImage(&gamma);

	cvReleaseImage(&src);
	cvReleaseImage(&equal);
	cvReleaseImage(&tmp);
}