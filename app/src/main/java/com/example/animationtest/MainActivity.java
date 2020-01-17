package com.example.animationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, testInterface {

    private EditText etSearchContent;
    private ImageButton ibtnSearch;
    private  InputMethodManager inputMethodManager;
    public static final int animationTime = 1000;

    private EditText etSearchContentSecond;
    private ImageButton ibtnSearchSecond;
    private static final String TAG = "MainActivity";
    int posX,posY;
    private FrameLayout.LayoutParams layoutParams;
    private FrameLayout secondContainer;
    private TextView tv_secContainer;
    private int widthPixels;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchContent = findViewById(R.id.et_search_content);
        ibtnSearch = findViewById(R.id.ibtn_search);
        floatingActionButton = findViewById(R.id.fab_temp);
        ibtnSearchSecond = findViewById(R.id.ibtn_container_sec_search);

        secondContainer = findViewById(R.id.secondContainer);
        tv_secContainer = findViewById(R.id.tv_secContainer);
        etSearchContentSecond = findViewById(R.id.et_container_sec_search_content);

        layoutParams = (FrameLayout.LayoutParams) ibtnSearchSecond.getLayoutParams();


        inputMethodManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int widthPixels = displayMetrics.widthPixels;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragment testFragment = new TestFragment(MainActivity.this);
                testFragment.show(getSupportFragmentManager(),"test fragment");
            }
        });

        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ibtnSearch,"translationX",widthPixels);
                objectAnimator.setDuration(animationTime);
                objectAnimator.start();

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
                fadeOut.setDuration(animationTime);
                ibtnSearch.setAnimation(fadeOut);

                Animation fadeIn = new AlphaAnimation(0,1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(animationTime*2);

                etSearchContent.setVisibility(View.VISIBLE);
                etSearchContent.setAnimation(fadeIn);
                etSearchContent.requestFocus();
                inputMethodManager.showSoftInput(etSearchContent,InputMethodManager.SHOW_IMPLICIT);
            }
        });


        etSearchContentSecond.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    etSearchContentSecond.setVisibility(View.VISIBLE);
                    etSearchContentSecond.setVisibility(View.INVISIBLE);
                    ibtnSearchSecond.setAlpha(1.0f);

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ibtnSearchSecond, "X", 30);
                    objectAnimator.setDuration(100);
                    objectAnimator.start();

                    layoutParams.leftMargin = 30;
                    ibtnSearchSecond.setLayoutParams(layoutParams);

                }
                return true;
            }
        });

        etSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                    String res = etSearchContent.getText().toString().trim();
                    if(!TextUtils.isEmpty(res)) {
                        Toast.makeText(getApplicationContext(), "Searching for " + etSearchContent.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ibtnSearch,"translationX",16.0f);
                        objectAnimator.setDuration(animationTime);
                        objectAnimator.start();

                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                        fadeIn.setDuration(animationTime);
                        ibtnSearch.setAnimation(fadeIn);

                        Animation fadeOut = new AlphaAnimation(1,0);
                        fadeOut.setInterpolator(new DecelerateInterpolator());
                        fadeOut.setDuration(animationTime);
                        etSearchContent.setAnimation(fadeOut);
                        etSearchContent.setText("");
                        etSearchContent.setVisibility(View.INVISIBLE);

                    }
                }
                return false;
            }
        });





        ibtnSearchSecond.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:

                      //  Log.i(TAG, "onTouch: Y = "+event.getRawY());
                        layoutParams.leftMargin = (int) event.getRawX();
                        Log.i(TAG, "onTouch: left margin is "+layoutParams.leftMargin);



                        if(layoutParams.leftMargin<=widthPixels-100 && layoutParams.leftMargin>=30) {
                            ibtnSearchSecond.setLayoutParams(layoutParams);
                            Log.i(TAG, "onTouch: X = "+event.getRawX());
                            float alphaValue =  (widthPixels-200)-(int)event.getRawX();
                            ibtnSearchSecond.setAlpha(alphaValue/1000);
                            etSearchContentSecond.setAlpha(0.8f-(alphaValue/1000));
                            etSearchContentSecond.setVisibility(View.VISIBLE);
                        }else if(layoutParams.leftMargin>=(widthPixels-200)){
                            ibtnSearchSecond.setVisibility(View.INVISIBLE);
                            etSearchContentSecond.setVisibility(View.VISIBLE);
                        }else if(layoutParams.leftMargin<=24){
                            etSearchContentSecond.setVisibility(View.INVISIBLE);
                        }

                        if(ibtnSearchSecond.getVisibility()==View.INVISIBLE){
                            etSearchContentSecond.setVisibility(View.VISIBLE);
                            etSearchContentSecond.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        }else{
                            etSearchContentSecond.setInputType(InputType.TYPE_NULL);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        layoutParams.leftMargin = (int) event.getRawX();
                        Log.i(TAG, "onTouch: UPPPPPPPPPPPPPPPPPPPP " + layoutParams.leftMargin);

                        if (ibtnSearchSecond.getVisibility() == View.VISIBLE ) {
                            etSearchContentSecond.setVisibility(View.INVISIBLE);
                            ibtnSearchSecond.setAlpha(1.0f);

                            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ibtnSearchSecond, "X", 30);
                                objectAnimator.setDuration(100);
                                objectAnimator.start();

                            layoutParams.leftMargin = 30;
                            ibtnSearchSecond.setLayoutParams(layoutParams);

                        }else{
                            etSearchContentSecond.requestFocus();
                            etSearchContentSecond.setAlpha(1.0f);
                            inputMethodManager.showSoftInput(etSearchContentSecond,InputMethodManager.SHOW_IMPLICIT);
                        }


                        break;
                }
                return true;
            }
        });



    }

    public void showDataFromFragment(String data){
        ((TextView)findViewById(R.id.textDataFromFrag)).setText(data);
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Rect rectf = new Rect();
        tv_secContainer.getGlobalVisibleRect(rectf);
        Log.i(TAG, "onResume: "+rectf.left);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        Log.i(TAG, "onDrag: "+event.getX());
        return true;
    }

    @Override
    public void showResultData(String data) {
        ((TextView)findViewById(R.id.textDataFromFrag)).setText(data);
    }
}
