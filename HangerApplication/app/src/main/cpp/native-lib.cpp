#include <jni.h>
#include <string>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>


#include <iostream>

using namespace std;
using namespace cv;

const Scalar RED = Scalar(0,0,255);
const Scalar PINK = Scalar(230,130,255);
const Scalar BLUE = Scalar(255,0,0);
const Scalar LIGHTBLUE = Scalar(255,255,160);
const Scalar GREEN = Scalar(0,255,0);
const Scalar WHITE = Scalar(255,255,255);

JNIEnv* thisEnv;
jobject thisObj;
JavaVM  *jvm;

static void getBinMask( const Mat& comMask, Mat& binMask )
{
   // if(&comMask==NULL||&binMask==NULL){ return;}

    if( comMask.empty() || comMask.type()!=CV_8UC1 )
        CV_Error( Error::StsBadArg, "comMask is empty or has incorrect type (not CV_8UC1)" );
    if( binMask.empty() || binMask.rows!=comMask.rows || binMask.cols!=comMask.cols ) {
        binMask.create(comMask.size(), CV_8UC1);
        //binMask.setTo(Scalar(255,255,255));
    }
    binMask = comMask & 1;
    //binMask = comMask & (Scalar(255,255,255)); 이렇게 하니까 네모가 그냥 크롭되는 현상이 발생함

}

class GCApplication
{
public:
    enum{ NOT_SET = 0, IN_PROCESS = 1, SET = 2 };
    static const int radius = 7;
    static const int thickness = -1;

    GCApplication();
    ~GCApplication();
    void reset();
    void setImageAndShowId(Mat *_image, jmethodID _showId );
    void showImage(JNIEnv *env, jobject instance) const;
    void mouseClick( int event, int x, int y, int flags, JNIEnv *env, jobject instance);
    int nextIter();
    int getIterCount() const { return iterCount; }

    //새로 추가~
    void setting();
    //~새로 추가

private:
    void setRectInMask();
    void setLblsInMask( int flags, Point p ,bool isPr);

    const Mat* image;
    jmethodID showId;


    Mat mask;
    Mat bgdModel, fgdModel;

    uchar rectState, lblsState, prLblsState;
    bool isInitialized;

    Rect rect;
    vector<Point> fgdPxls, bgdPxls, prFgdPxls, prBgdPxls;
    int iterCount;
};
GCApplication::GCApplication(){

}
GCApplication::~GCApplication(){

}
void GCApplication::reset()
{
    if( !mask.empty() )
        mask.setTo(Scalar::all(GC_BGD));
    bgdPxls.clear(); fgdPxls.clear();
    prBgdPxls.clear();  prFgdPxls.clear();

    isInitialized = false;
    rectState = NOT_SET;
    lblsState = NOT_SET;
    prLblsState = NOT_SET;
    iterCount = 0;
}

void GCApplication::setting() {
    //isInitialized = false;
    rectState = NOT_SET;
    //iterCount = 0;
    //isInitialized = true;
    //rect = Rect(mask.cols/2,mask.rows/2,mask.cols,mask.rows);
}



void GCApplication::setImageAndShowId(Mat *_image, jmethodID _showId )
{
   // if(_image==NULL||_showId==NULL){ return;}

    if( _image->empty())
        return;
    image = _image;
    showId = _showId;
    mask.create(image->size(), CV_8UC1);
    //mask.setTo(Scalar(255,255,255));
    reset();
}

void GCApplication::showImage(JNIEnv *env, jobject instance) const
{
    //if(env==NULL||instance==NULL){ return;}//추가

    if( image->empty() )
        return;

    Mat *res= new Mat(image->size(), CV_8UC3);
    //Mat *res = new Mat;

    Mat binMask;

    Mat white(image->size(), CV_8UC3); white.setTo(WHITE);

    Mat mymask(image->size(), CV_8UC3);
    Mat mymask_inv(image->size(), CV_8UC3);

    Mat myimg(image->size(), CV_8UC3);

    if( !isInitialized ){
        image->copyTo( *res );
    } ////한번도 사각형그리고 "확정"안눌렀으면 이미지 전체가 res에 복사되고 res가 showimage됨. 사용자에게는 아무일도 안일어난 것 처럼 보임
    else
    {
        getBinMask( mask, binMask );
        image->copyTo( *res, binMask );

        white.copyTo(mymask, binMask); //mymask 전경=흰색, 배경=검은색
        bitwise_not(mymask,mymask_inv); //mymask_inv 전경 =검은색, 배경=흰색

        //res.copyTo(mymask_inv);
        add(*res,mymask_inv,myimg);
        myimg.copyTo(*res);
        //res.copyTo(mymask_inv);

        //image->copyTo(res,mymask_inv);

        //bitwise_and(res,mymask,img2_fg);
        //add(img2_fg,mymask_inv,res);

        //white.copyTo(binMask,mask); 크롭됨
        //res.setTo(Scalar(255,255,255)); 크롭됨
        //white.copyTo(res,mask); 사각형 크롭되어서 하얀색으로 나타남. binMask안에 사람모양이 있는듯?
        //res = binMask;//다 까매짐
    }

    vector<Point>::const_iterator it;
    for( it = bgdPxls.begin(); it != bgdPxls.end(); ++it )
        circle( *res, *it, radius, BLUE, thickness );
    for( it = fgdPxls.begin(); it != fgdPxls.end(); ++it )
        circle( *res, *it, radius, RED, thickness );
    for( it = prBgdPxls.begin(); it != prBgdPxls.end(); ++it )
        circle( *res, *it, radius, LIGHTBLUE, thickness );
    for( it = prFgdPxls.begin(); it != prFgdPxls.end(); ++it )
        circle( *res, *it, radius, PINK, thickness );

    if( rectState == IN_PROCESS || rectState == SET )//사각형 드래그를 하는 중이거나 사각형 드래그를 끝냈으면
        rectangle( *res, Point( rect.x, rect.y ), Point(rect.x + rect.width, rect.y + rect.height ), GREEN, 2);
    long long*img = new long long;
    *img = (long long)res;

    if(*img!=0&&*img!=NULL)
        env->CallVoidMethod(instance,showId,*img);

    delete img;
    res->release();

    //long img = (long) &res;
    //env->CallVoidMethod(instance,showId,img);
}

void GCApplication::setRectInMask()
{
    CV_Assert( !mask.empty() );
    //mask.setTo(Scalar(GC_BGD));
    mask.setTo( GC_BGD );
    rect.x = max(0, rect.x);
    rect.y = max(0, rect.y);
    rect.width = min(rect.width, image->cols-rect.x);
    rect.height = min(rect.height, image->rows-rect.y);
    (mask(rect)).setTo( Scalar(GC_PR_FGD) );
}

void GCApplication::setLblsInMask( int flags, Point p, bool isPr )
{
  //  if(flags==NULL||isPr==NULL){ return;}

    vector<Point> *bpxls, *fpxls;
    uchar bvalue, fvalue;
    if( !isPr )
    {
        bpxls = &bgdPxls;
        fpxls = &fgdPxls;
        bvalue = GC_BGD;
        fvalue = GC_FGD;
    }
    else
    {
        bpxls = &prBgdPxls;
        fpxls = &prFgdPxls;
        bvalue = GC_PR_BGD;
        fvalue = GC_PR_FGD;
    }
    if( flags == 2 )
    {
        bpxls->push_back(p);
        circle( mask, p, radius, bvalue, thickness );
    }
    if( flags  == 1)
    {
        fpxls->push_back(p);
        circle( mask, p, radius, fvalue, thickness );
    }
}
// event:DOWN = 0,UP = 1,MOVE = 2
void GCApplication::mouseClick( int event, int x, int y, int flags ,JNIEnv *env, jobject instance)
{

    // TODO add bad args check
    switch(event){
        case 0: {
            if (flags == 0 && rectState == NOT_SET) {
                rectState = IN_PROCESS;
                rect = Rect(x, y, 1, 1);//이거 없애니까 드래그 하면 0,0부터 시작함
            }
            if ( flags == 1 && rectState == SET ) //사각형드래그를 끝내고 전경을 누르면()
                lblsState = IN_PROCESS;
            if ( flags == 2 && rectState == SET )
                prLblsState = IN_PROCESS;
        }
            break;
        case 1:{
            if(flags == 0 || flags == 1){
                if( rectState == IN_PROCESS )
                {
                    rect = Rect( Point(rect.x, rect.y), Point(x,y) );
                    rectState = SET; // 여기서 사각형이 SET된다.
                    setRectInMask();
                    CV_Assert( bgdPxls.empty() && fgdPxls.empty() && prBgdPxls.empty() && prFgdPxls.empty() );
                    showImage(env,instance);
                }
                if( lblsState == IN_PROCESS )
                {
                    setLblsInMask(flags, Point(x,y),false);
                    lblsState = SET;
                    showImage(env,instance);
                }
            }
            if(flags == 2 && prLblsState == IN_PROCESS ){
                setLblsInMask(flags, Point(x,y),false);
                prLblsState = SET;
                showImage(env,instance);
            }
        }
            break;
        case 2:{
            if( rectState == IN_PROCESS )
            {
                rect = Rect( Point(rect.x, rect.y), Point(x,y) );
                CV_Assert( bgdPxls.empty() && fgdPxls.empty() && prBgdPxls.empty() && prFgdPxls.empty() );
                showImage(env,instance);
            }
            else if( lblsState == IN_PROCESS )
            {
                setLblsInMask(flags, Point(x,y),false);
                showImage(env,instance);
            }
            else if( prLblsState == IN_PROCESS )
            {
                setLblsInMask(flags, Point(x,y),false);
                showImage(env,instance);
            }
        }
            break;
    }

}

int GCApplication::nextIter()
{
    if( isInitialized ) //이미 한번 "확정"버튼 눌렀으면
        grabCut( *image, mask, rect, bgdModel, fgdModel, 1 ); //확정 -> 범위(사각형NOTSET&&이니셜라이즈TRUE)->확정했을때 걸리는 곳.
    else //처음 "확정"버튼 눌렀을 때
    {
        if( rectState != SET ) //아직 사각형이 SET되지 않은 상태(아무것도 안건들인 상태)
            return iterCount; //iterCount의 초기값은 0. =>iterCount가 기존에서 변하지 않았기

        if( lblsState == SET || prLblsState == SET )
            grabCut( *image, mask, rect, bgdModel, fgdModel, 1, GC_INIT_WITH_MASK ); //반복횟수 늘리면 결과 좋아지나?
        else
            grabCut( *image, mask, rect, bgdModel, fgdModel, 1, GC_INIT_WITH_RECT );

        isInitialized = true;
    }
    iterCount++;

    bgdPxls.clear(); fgdPxls.clear();
    prBgdPxls.clear(); prFgdPxls.clear();
    return iterCount;
}

;

static void on_mouse(GCApplication *gcapp, int event, int x, int y, int flags,JNIEnv *env, jobject instance)
{
    //if(gcapp==NULL){ return;}
    gcapp->mouseClick( event, x, y, flags ,env,instance);
}





extern "C"
JNIEXPORT GCApplication* JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_initGrabCut(JNIEnv *env, jobject instance,
                                                                     jlong image) {

    // TODO

    thisEnv=env;
    thisObj=instance;
    env->GetJavaVM(&jvm);

    Mat *img = (Mat *) image ;
    GCApplication *gcapp = new GCApplication();


    jvm->AttachCurrentThread(&thisEnv, 0); //this is important to avoid threading errors
    jclass jc = thisEnv->GetObjectClass(thisObj);
    jmethodID showId = thisEnv->GetMethodID(jc, "showImage", "(J)V");
    gcapp->setImageAndShowId(img, showId);
    gcapp->showImage(thisEnv, thisObj);

    //env->DeleteLocalRef(instance); //추가
    //env->DeleteLocalRef(jc); //추가

    return gcapp;


}extern "C"
JNIEXPORT void JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_moveGrabCut(JNIEnv *env, jobject instance,
                                                                     jint event, jint x, jint y,
                                                                     jint flags, jlong gcapp) {

    // TODO
    GCApplication *g = (GCApplication *) gcapp;
    //if(gcapp==NULL){ return;}

    on_mouse(g,event,x,y,flags,env,instance);
    env->DeleteLocalRef(instance);

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_reset(JNIEnv *env, jobject instance,
                                                               jlong gcapp) {
    //if(gcapp==NULL||env==NULL||instance==NULL){ return;}

    // TODO
    GCApplication *g = (GCApplication *) gcapp;
    g->reset();
    g->showImage(env,instance);
    env->DeleteLocalRef(instance);

}extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_grabCut(JNIEnv *env, jobject instance,
                                                                 jlong gcapp) {
    //if(gcapp==NULL||env==NULL||instance==NULL){ return true;}

    // TODO
    GCApplication *g = (GCApplication *) gcapp;

    int iterCount = g->getIterCount(); //IterCount를 리턴한다.
    int newIterCount = g->nextIter();
    jboolean  result = (jboolean)(newIterCount > iterCount);
    env->DeleteLocalRef(instance);
    return result;
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_grabCutOver(JNIEnv *env, jobject instance,
                                                                     jlong gcapp) {
    //if(gcapp==NULL||env==NULL||instance==NULL){ return;}

    // TODO
    GCApplication *g = (GCApplication *) gcapp;
    g->showImage(env,instance);
    //env->DeleteLocalRef(instance); //추가
    env->DeleteLocalRef(instance);
    return;
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_geehy_hangerapplication_GrabcutActivity2_resultSetting(JNIEnv *env,
                                                                        jobject instance,
                                                                        jlong gcapp) {
    // TODO
    GCApplication *g = (GCApplication *) gcapp;
    g->setting();
    g->showImage(env,instance);
    env->DeleteLocalRef(instance);
}

//JNIEXPORT jlong JNICALL Java_org_opencv_samples_facedetect_DetectionBasedTracker_nativeSkinFilter (JNIEnv *, jclass, jobject, jlong);
