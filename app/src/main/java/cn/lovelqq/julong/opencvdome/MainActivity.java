package cn.lovelqq.julong.opencvdome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="MainActivity";
    private Button bt_r,bt_b,bt_y;
    private ImageView imageView;
    private TextView tv_content;
    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV not loaded");
        }
        else {
            Log.d(TAG,"OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * 加载控件初始化
     */
    private void init(){
        bt_r=findViewById(R.id.bt_r);
        bt_b=findViewById(R.id.bt_b);
        bt_y=findViewById(R.id.bt_y);
        imageView=findViewById(R.id.imageView);
        tv_content=findViewById(R.id.tv_Content);

        bt_r.setOnClickListener(this);
        bt_b.setOnClickListener(this);
        bt_y.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        OpenCOLOR open=new OpenCOLOR();
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.i16);
        Bitmap bitmapsrc=bitmap;
        switch (view.getId()){
            case R.id.bt_r:
                Mat srcr= open.main(bitmap,"r");
                Utils.matToBitmap(srcr,bitmapsrc);
                imageView.setImageBitmap(bitmapsrc);
                tv_content.setText(OpenCOLOR.jieGuo);
                break;
            case R.id.bt_b:
                Mat srcb=  open.main(bitmap,"b");
                Utils.matToBitmap(srcb,bitmapsrc);
                imageView.setImageBitmap(bitmapsrc);
                tv_content.setText(OpenCOLOR.jieGuo);
                break;
            case R.id.bt_y:
               Mat srcy= open.main(bitmap,"y");
                Utils.matToBitmap(srcy,bitmapsrc);
                imageView.setImageBitmap(bitmapsrc);
                tv_content.setText(OpenCOLOR.jieGuo);
                break;
        }
    }
}
