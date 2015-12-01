#include <cv.h>
#include <highgui.h>
#include <cxcore.h>
#include <cvaux.h>

class DifferenceOfGaussian{
private:
	int _kernelSize;
	int _sigma1, _sigma2;

public:
	DifferenceOfGaussian(int kernelSize, int sigma1, int sigma2);
	void output(IplImage* src, IplImage* dst);

};