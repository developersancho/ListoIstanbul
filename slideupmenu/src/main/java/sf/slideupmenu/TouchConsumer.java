package sf.slideupmenu;

/**
 * @author pa.gulko zTrap (12.07.2017)
 */
class TouchConsumer {
    SlideUpBuilder mBuilder;
    AnimationProcessor mAnimationProcessor;
    float mMaxSlidePosition;
    
    boolean mCanSlide = true;
    LoggerNotifier mNotifier;
    
    float mViewHeight;
    float mViewWidth;
    
    float mStartPositionY;
    float mStartPositionX;
    float mViewStartPositionY;
    float mViewStartPositionX;
    
    TouchConsumer(SlideUpBuilder builder, LoggerNotifier notifier,
                  AnimationProcessor animationProcessor){
        mBuilder = builder;
        mAnimationProcessor = animationProcessor;
        mNotifier = notifier;
    }
    
    int getEnd(){
        if (mBuilder.mIsRTL){
            return mBuilder.mSliderView.getLeft();
        }else {
            return mBuilder.mSliderView.getRight();
        }
    }
    
    int getStart(){
        if (mBuilder.mIsRTL){
            return mBuilder.mSliderView.getRight();
        }else {
            return mBuilder.mSliderView.getLeft();
        }
    }
    
    int getTop(){
        return mBuilder.mSliderView.getTop();
    }
    
    int getBottom(){
        return mBuilder.mSliderView.getBottom();
    }
}
