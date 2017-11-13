package bindview.coder.allen.com.abindview;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.LayoutId;

import bindview.coder.allen.com.abindview.switchBuk.ICaseAction;
import bindview.coder.allen.com.abindview.switchBuk.SwichCase;

import static bindview.coder.allen.com.abindview.MainActivity$$Layout.hello;
import static bindview.coder.allen.com.abindview.MainActivity$$Layout.text;

/**
 * @author husongzhen
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @LayoutId(value = "activity_main", path = "app/src/main/res/layout/")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity$$Layout mainActivity$$Layout = new MainActivity$$Layout();
        mainActivity$$Layout.inject(this);
        text.setOnClickListener(this);
        hello.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        SwichCase.create()
                .scase(R.id.hello, new ICaseAction() {
                    @Override
                    public void onAction() {
                        helloMethod(view);
                    }
                })
                .scase(R.id.text, new ICaseAction() {
                    @Override
                    public void onAction() {
                        textMethod(view);
                    }
                })
                .action(view.getId());
    }

    private void textMethod(View view) {
        showToast(view);
    }

    private String getNameById(View view) {
        return getName(view.getId());
    }

    private void helloMethod(View view) {
        showToast(view);
    }

    private void showToast(View view) {
        Toast.makeText(this, "hello world" + "name == " + getNameById(view), Toast.LENGTH_SHORT).show();
    }


    public String getName(int id) {
        Resources res = getResources();
        return res.getResourceName(id);
    }


}
