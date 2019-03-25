package com.michael.clearredweixin;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class ClearRedService extends AccessibilityService{
    private static final String TAG = "ClearRedService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Toast.makeText(getApplicationContext(), "clear", Toast.LENGTH_SHORT).show();
//        Log.i(TAG, ""+event.getEventType());
        if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            launchWX();
            sleep(5000L);

            AccessibilityNodeInfo root = getRootInActiveWindow();
            int count = getCount(root);

            AccessibilityNodeInfo firstTabNode = getFirstTabNode(root);

            Log.i(TAG, "firstTabNode:" + firstTabNode);
            if(firstTabNode!=null){
                for (int i = 0; i < count; i++) {
                    Log.i(TAG, "i:: " + i);
                    firstTabNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(50L);
                    firstTabNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(2000L);

                }
            }
        }
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AccessibilityNodeInfo getFirstTabNode(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> temp = root.findAccessibilityNodeInfosByText("微信");

        Log.i(TAG, "temp " + temp);
        if(temp!=null&&!temp.isEmpty()){
            Log.i(TAG, "temp.size: " + temp.size());
            for (int i = 0; i < temp.size(); i++) {
                AccessibilityNodeInfo t = temp.get(i);
                Log.i(TAG, " : "+i+" " + t.toString());
                if("微信".contentEquals(t.getText())){
                    return t;
                }
            }
        }
        return null;
    }

    private int getCount(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> temp = root.findAccessibilityNodeInfosByText("微信(");
        AccessibilityNodeInfo countNode = null;

        Log.i(TAG, "temp " + temp);
        if(temp!=null&&!temp.isEmpty()){
            Log.i(TAG, "temp.size: " + temp.size());
            for (int i = 0; i < temp.size(); i++) {
                AccessibilityNodeInfo t = temp.get(i);
                Log.i(TAG, " : "+i+" " + t.toString());
            }
            countNode = temp.get(0);
        }

        if(countNode!=null) {
            String text = (String) countNode.getText();
            String countStr = text.substring(3, text.indexOf(")"));
            int count = Integer.parseInt(countStr);
            Log.i(TAG, "COUNT:: " + count);
            return count;
        }
        return 0;
    }

    private void launchWX() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        startActivity(intent);
    }

    @Override
    public void onInterrupt() {

    }
}
