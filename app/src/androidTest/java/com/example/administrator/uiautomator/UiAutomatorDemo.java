package com.example.administrator.uiautomator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Test;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class UiAutomatorDemo {

    private final String TAG = "LYP";
    private String mPackageName = "com.chaoxing.mobile";
    private String mLaunchActivity = ".main.ui.MainTabActivity";
    boolean result;
    int timeOut = 5000;

    @Test
    public void main() {
        UiDevice mDevice = getDevice();
        if (mDevice == null) {
            return;
        }
        Log.i(TAG, "mDevice: " + mDevice.getProductName());

        try {
            if (!mDevice.isScreenOn()) {
                mDevice.wakeUp();//唤醒屏幕
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        enterApk(mDevice);


        //startAPP(mDevice, mPackageName, mLaunchActivity);  //启动app
        //mDevice.waitForWindowUpdate(mPackageName, 5 * 2000);//等待app
        //closeAPP(mDevice, mPackageName);//关闭app
    }

    public void enterApk(UiDevice uiDevice) {

        // clearBgTask(mDevice);  不能清理后台,会杀掉测试进程挂掉

        try {
            uiDevice.pressHome();
            uiDevice.pressHome();
            sleep(1000);
            closeAPP(uiDevice, mPackageName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean flag = true;
        while (flag) {
            swipeLeft(uiDevice, 20);
            //swipeRight(mDevice,20);
            UiObject searchObject = new UiObject(new UiSelector().text("学习通"));
            Log.i(TAG, "isExist 学习通：" + String.valueOf(searchObject.exists()));
            if (searchObject.exists()) {
                try {
                    searchObject.click();
                    searchObject.clickAndWaitForNewWindow(5);
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "StartApp Success");
                flag = false;
            }
        }
        Log.i(TAG, "Exit Swipe");

        // 进入课程
        UiObject2 ui = uiDevice.findObject(By.res("com.chaoxing.mobile:id/iv_home_course"));
        if (ui != null) {
            ui.click();
            uiDevice.waitForWindowUpdate(mPackageName, timeOut);
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 大学生安全教育
        ui = uiDevice.findObject(By.textStartsWith("大学生安全教育"));
        if (ui != null) {
            ui.click();
            uiDevice.waitForWindowUpdate(mPackageName, timeOut);
        }

        listIteator(uiDevice);

    }


    public void listIteator(UiDevice uiDevice) {

        boolean flag = true;
        int jump = 6;
        while (flag) {
            // 找到待完成listview顶部控件
            UiObject2 titlte = uiDevice.findObject(By.res("com.chaoxing.mobile:id/appbar"));
            int titleC = titlte.getVisibleBounds().centerY();
            int listB = uiDevice.getDisplayHeight();
            int widthR = uiDevice.getDisplayWidth();
            //Log.i(TAG, "titleC: " + titleC + " listB: " + listB + " widthR: " + widthR);
            if (jump != 0) {
                uiDevice.swipe(widthR / 2, listB, widthR / 2, titleC, 80);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jump --;
                continue;
            }

            float height = 0;
            //使用By创建BySelector，并用findObject返回UiObject2
            UiObject2 list = uiDevice.findObject(By.res("com.chaoxing.mobile:id/rv_knowledge"));
            //list.getChildren()得到子控件列表的UiObject2

            int count = list.getChildren().size();
            Log.i(TAG, "list count: " + count);

            for (int i = 0; i < count - 1; i++) {

                UiObject2 itemi = list.getChildren().get(i);
                String resId = itemi.getChildren().get(0).getResourceName();

                //Log.i(TAG, "list resId: " + resId);
                if (resId.equals("com.chaoxing.mobile:id/sub_node")) {
                    try {
                        String text = itemi.getChildren().get(0).getChildren().get(0).getChildren()
                                .get(0).getChildren().get(2).getText();
                        Log.i(TAG, "list item text:" + text);
                        if (itemi != null) {
                            itemi.click();
                            // 处理进入课程后的任务
                            enterItem(uiDevice);
                            try {
                                sleep(3000);
                                uiDevice.pressBack();
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
            uiDevice.swipe(widthR / 2, listB, widthR / 2, titleC, 80);
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void enterItem(UiDevice uiDevice) {
        uiDevice.waitForWindowUpdate(mPackageName, timeOut);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject2 study = uiDevice.findObject(By.text("任务点已完成"));
        if (study != null) {
            Log.i(TAG, "study status:" + study.getText());
        } else {
            Log.i(TAG, "study status:" + "任务点未完成");
            UiObject2 playBtn = uiDevice.findObject(By.clazz("android.widget.Image").text("play"));
            if (playBtn != null) {
                playBtn.click();
                try {
                    sleep(3000);
                    uiDevice.click(2064, 1024);
                    uiDevice.waitForWindowUpdate(null, timeOut);
                    sleep(1000);
                    uiDevice.click(1966, 832);
                    uiDevice.waitForWindowUpdate(null, timeOut);
                    sleep(1000);
                    uiDevice.click(800, 530);
                    Log.i(TAG, "video play speed 2x");
                    sleep(2000);
                    checkProgress(uiDevice);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void checkProgress(UiDevice uiDevice) {

        boolean loop = true;
        while(loop) {
            Log.i(TAG,"检查视频播放进度");
            UiObject2 stop = uiDevice.findObject(By.res("com.chaoxing.mobile:id/start"));
            UiObject2 question = uiDevice.findObject(By.res("com.chaoxing.mobile:id/btn_check_answer"));

            // 判断视频是否结束or出现题目
            if (question != null || stop != null) {
                loop = false;
            }
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "视频播放结束");
        uiDevice.pressBack();
    }

    public void clearBgTask(UiDevice uiDevice) {
        try {
            uiDevice.pressHome();
            sleep(1000);
            uiDevice.pressRecentApps();
            sleep(1000);
            UiObject2 ui = uiDevice.findObject(By.res("com.huawei.android.launcher:id/clear_all_recents_image_button"));
            if (ui != null) {
                ui.click();
            }
            sleep(2000);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //向左滑动
    public void swipeLeft(UiDevice uiDevice, int step) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int x = uiDevice.getDisplayWidth();//获取屏幕的宽
        int y = uiDevice.getDisplayHeight();//获取屏幕的高
        Log.i(TAG, "屏幕分辨率: " + x + " X " + y);
        uiDevice.swipe((int) (x / 1.3), y / 2, x / 6, y / 2, step);//左滑
        Log.i(TAG, "左滑");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //向右滑动
    public void swipeRight(UiDevice uiDevice, int step) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int x = uiDevice.getDisplayWidth();//获取屏幕的宽
        int y = uiDevice.getDisplayHeight();//获取屏幕的高
        Log.i(TAG, "屏幕分辨率: " + x + " X " + y);
        uiDevice.swipe(x / 6, y / 2, (int) (x / 1.3), y / 2, step);//右滑
        Log.i(TAG, "右滑");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //向下滑动
    public void swipeDown(UiDevice uiDevice, int step) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int x = uiDevice.getDisplayWidth();//获取屏幕的宽
        int y = uiDevice.getDisplayHeight();//获取屏幕的高
        uiDevice.swipe(x / 2, y / 6, x / 2, (int) (y / 1.2), step);//下滑
        Log.i(TAG, "下滑");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public UiDevice getDevice() {
        UiDevice mUIDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (mUIDevice != null) {
            Log.i(TAG, "get Device successfully");
            return mUIDevice;
        } else {
            Log.i(TAG, "get Device failed");
            return null;
        }
    }

    private void startAPP(String sPackageName) {
        Context mContext = InstrumentationRegistry.getContext();
        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(sPackageName);  //通过Intent启动app
        mContext.startActivity(myIntent);
    }

    private void closeAPP(UiDevice uiDevice, String sPackageName) {
        Log.i(TAG, "closeAPP: ");
        try {
            uiDevice.executeShellCommand("am force-stop " + sPackageName);//通过命令行关闭app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAPP(UiDevice uiDevice, String sPackageName, String sLaunchActivity) {
        try {
            uiDevice.executeShellCommand("am start -n " + sPackageName + "/" + sLaunchActivity);//通过命令行启动app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}