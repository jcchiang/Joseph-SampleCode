#include "DifferenceOfGaussian.h"

DifferenceOfGaussian::DifferenceOfGaussian(int kernelSize, int sigma1, int sigma2){
	_kernelSize = kernelSize;
	_sigma1 = sigma1;
	_sigma2 = sigma2;
}

void DifferenceOfGaussian::output(IplImage* src, IplImage* dst){
	CvArr* dog1 = cvCreateImage(cvGetSize(src), src->depth, src->nChannels);
	CvArr* dog2 = cvCreateImage(cvGetSize(src), src->depth, src->nChannels);
	cvSmooth(src, dog1, CV_GAUSSIAN, _kernelSize, _kernelSize, _sigma1, _sigma1);
	cvSmooth(src, dog2, CV_GAUSSIAN, _kernelSize, _kernelSize, _sigma2, _sigma2);
	cvSub(dog2, dog1, dst, 0);
}