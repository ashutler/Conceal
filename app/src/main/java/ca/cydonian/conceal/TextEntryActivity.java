package ca.cydonian.conceal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class TextEntryActivity extends Activity {

    TranslateTextAccessibilityService mService;
    private EditText mMessageText;
    private Button mInsertButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry);

        mService = TranslateTextAccessibilityService.getSharedInstance();

        mMessageText = (EditText)findViewById(R.id.messageText);
        mInsertButton = (Button)findViewById(R.id.insertButton);
        mCancelButton = (Button)findViewById(R.id.cancelButton);
        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService != null) {
                    char[] text = new char[mMessageText.getText().length()];
                    for (int i = 0; i < mMessageText.getText().length(); i++) {
                        text[i] = (char)((int)mMessageText.getText().charAt(i) - 3);
                    }
                    mService.setEncryptedText(String.valueOf(text));
                } else {
                    mService.setEncryptedText(mMessageText.getText().toString());
                }
                finish();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.text_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
