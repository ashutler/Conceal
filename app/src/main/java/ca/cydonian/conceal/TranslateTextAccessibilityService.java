package ca.cydonian.conceal;


import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

public class TranslateTextAccessibilityService extends AccessibilityService {
    private static final String TAG = TranslateTextAccessibilityService.class.getName();
    private static TranslateTextAccessibilityService sSharedInstance;

    /** The class name of TaskListView - for simplicity we speak only its items. */
    private static final String LIST_VIEW_CLASS_NAME =
            "android.widget.ListView";
    private static final String TEXT_VIEW_CLASS_NAME =
            "android.widget.TextView";
    public TranslateTextAccessibilityService() {
    }
    private AccessibilityNodeInfo mTextEntryNode;
    private static Handler handler;

    private String mEncryptedText;

    @Override
    protected void onServiceConnected() {
        sSharedInstance = this;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTextEntryNode.performAction(AccessibilityNodeInfo.ACTION_SELECT);
                mTextEntryNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }

        };

//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//
//// Set the type of events that this service wants to listen to.  Others
//        // won't be passed to this service.
//        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
//                AccessibilityEvent.TYPE_VIEW_SCROLLED;
//
//        // If you only want this service to work with specific applications, set their
//        // package names here.  Otherwise, when the service is activated, it will listen
//        // to events from all applications.
//        info.packageNames = new String[]
//                {"com.google.android.talk"};
//
//        // Set the type of feedback your service will provide.
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
//
//        // Default services are invoked only if no package-specific ones are present
//        // for the type of AccessibilityEvent generated.  This service *is*
//        // application-specific, so the flag isn't necessary.  If this was a
//        // general-purpose service, it would be worth considering setting the
//        // DEFAULT flag.
//
//        // info.flags = AccessibilityServiceInfo.DEFAULT;
//
//        info.notificationTimeout = 100;
//
//        this.setServiceInfo(info);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sSharedInstance = null;
        return super.onUnbind(intent);
    }
    public static TranslateTextAccessibilityService getSharedInstance() {
        return sSharedInstance;
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        AccessibilityNodeInfo nodeInfo = event.getSource();

        //AccessibilityNodeInfo listNode = getListNodeInfo(nodeInfo);

        if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            mTextEntryNode = event.getSource();

            displayDialog();
        }

//        if (listNode != null) {
//            changeNodeText(listNode);
//        }
    }

//    private void changeNodeText(AccessibilityNodeInfo listNode) {
//        int childCount = listNode.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            AccessibilityNodeInfo childNode = listNode.getChild(i);
//            if (childNode != null) {
//                String contentDescription = String.valueOf(childNode.getContentDescription());
//                if (contentDescription.length() > 0 && !contentDescription.equals("null")) {
//                    replaceText(this, childNode);
//                } else {
//                    changeNodeText(childNode);
//                }
//            }
//        }
//    }

    @Override
    public void onInterrupt() {

    }

    private AccessibilityNodeInfo getListNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (current != null) {
            if (LIST_VIEW_CLASS_NAME.equals(current.getClassName()))
                return current;

            // NOTE: Recycle the infos.
            AccessibilityNodeInfo oldCurrent = current;
            current = current.getParent();
            oldCurrent.recycle();
        }
        return null;
    }
    public void replaceText() {
        // Save previous contents to restore at the end
        //ClipData lastClip = clipboard.getPrimaryClip();

//        mTextEntryNode.performAction(AccessibilityNodeInfo.ACTION_SELECT);

//        Log.d("THE NODE INFO", newText);
//
//        ClipData clip = ClipData.newPlainText("label", newText);
//        clipboard.setPrimaryClip(clip);

        //Log.d("SENDING DATA", Boolean.toString(source.refresh()));

//        Bundle arguments = new Bundle();
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, 2);
//        source.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
       // source.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION);

//        Log.d("SENDING DATA", Boolean.toString(source
//                .performAction(AccessibilityNodeInfo.ACTION_PASTE)));

        // restore the previous content
        //clipboard.setPrimaryClip(lastClip);
    }
    public void displayDialog() {
        final WindowManager manager = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 1.0f;
        layoutParams.packageName = this.getPackageName();
        layoutParams.buttonBrightness = 1f;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        final View view = View.inflate(this.getApplicationContext(),R.layout.encrypt_options_layout, null);
        Button yesButton = (Button) view.findViewById(R.id.encryptButton);
        Button noButton = (Button) view.findViewById(R.id.cancelButton);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
                Intent enterText = new Intent(TranslateTextAccessibilityService.this, TextEntryActivity.class);
                enterText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enterText);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });
        manager.addView(view, layoutParams);
    }

    public String getEncryptedText() {
        return mEncryptedText;
    }

    public void setEncryptedText(String sEncryptedText) {
        this.mEncryptedText = sEncryptedText;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", mEncryptedText);
        clipboard.setPrimaryClip(clip);

        handler.sendEmptyMessage(0);
    }
}
