#include "faceDefine.h"

class FaceEqualCorrelizer{
	public:
		IplImage *tempImg, *eqlImg;
		void Equalizer(IplImage* srcImg, IplImage** dstImg);

		~FaceEqualCorrelizer(){
		};
	private:

};