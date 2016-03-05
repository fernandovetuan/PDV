package br.com.trainning.pdv.ui;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Created by android on 05/03/2016.
 */
public class BasicActivity extends AppCompatActivity
{

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}

