package asu.gunma.ui.util;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SimpleDirectionGestureDetector extends GestureDetector {
    public interface DirectionListener {
        void onLeft();

        void onRight();

        void onUp();

        void onDown();
    }

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else{
                if(velocityY>0){
                    directionListener.onDown();
                }else{
                    directionListener.onUp();
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY){

            return super.pan(x, y, deltaX, deltaY);
        }
    }

}
