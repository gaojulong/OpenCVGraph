package cn.lovelqq.julong.opencvdome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="MainActivity";
    private Button bton;
    private ImageView imageView;
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
        bton=findViewById(R.id.bton);
        imageView=findViewById(R.id.imageView);

        bton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bton:
                convert2Gray();
                break;
        }
    }
    private void convert2Gray(){
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.i464);
        Mat src=new Mat();
        Mat gray=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(gray,bitmap);
        imageView.setImageBitmap(bitmap);
    }

}
