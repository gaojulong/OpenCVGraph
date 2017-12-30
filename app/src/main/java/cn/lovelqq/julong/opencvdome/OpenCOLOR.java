package cn.lovelqq.julong.opencvdome;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * 
 * @author Junys
 *
 */
public class OpenCOLOR {

	/**
	 * ������
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);		
		System.out.println("Opencv�汾:"+Core.VERSION + "\n");
		new OpenCOLOR().ShowHelpText();
		// ��ȡͼ��·�������޸�
		Mat srcMat = Imgcodecs.imread("D://JavaWorkspace//HelloJava//src//ch0//16.jpg");
		Mat dstMat = srcMat.clone();
				
		Mat Rdst = new OpenCOLOR().findColor(srcMat, 0, 24);//�� ԭ��Χֵ 0~20 ����ͼƬɫ�ʴﲻ����׼�޸� 0~24
		Mat Gdst = new OpenCOLOR().findColor(srcMat, 60, 80);//�� ԭ��Χֵ 60~80
		Mat Ydst = new OpenCOLOR().findColor(srcMat, 30, 50);//��  ԭ��Χֵ 23~38 ����ͼƬɫ�ʴﲻ����׼�޸� 28~50

		Mat RdstImage = new OpenCOLOR().Graphicdetection(srcMat, Rdst, dstMat, "R");
		Mat GdstImage = new OpenCOLOR().Graphicdetection(srcMat, Gdst, dstMat, "G");
		Mat YdstImage = new OpenCOLOR().Graphicdetection(srcMat, Ydst, dstMat, "Y");
		
		// ����ͼ��·�������޸�
		Imgcodecs.imwrite("D://JavaWorkspace//HelloJava//src//ch0//17.jpg", RdstImage);
		Imgcodecs.imwrite("D://JavaWorkspace//HelloJava//src//ch0//18.jpg", GdstImage);
		Imgcodecs.imwrite("D://JavaWorkspace//HelloJava//src//ch0//19.jpg", YdstImage);
	}
	
	/**
	 * �����������ж���ɫʶ�����HSVģʽ
	 * @param srcMat
	 * @param min
	 * @param max
	 * @return
	 */
	public Mat findColor (Mat srcMat,int min,int max){
		Mat srcImage = srcMat.clone();
		Mat thresholded = new Mat();
		Mat hsv_image =new Mat() ;
		Imgproc.GaussianBlur(srcImage, srcImage, new Size(9,9),0 ,0);
		Imgproc.cvtColor(srcImage, hsv_image, Imgproc.COLOR_BGR2HSV);
		Core.inRange(hsv_image, new Scalar(min, 90, 90), new Scalar(max, 255, 255), thresholded);	
		return thresholded;
	}
	
	/**
	 * ������������֤������
	 * @param pt1
	 * @param pt2
	 * @param pt0
	 * @return
	 */
	public double angle(Point pt1,Point pt2,Point pt0) {
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		double ratio;//�߳�ƽ���ı�
		ratio = (dx1*dx1 + dy1*dy1) / (dx2*dx2 + dy2*dy2);
		if (ratio<0.8 || 1.2<ratio) {//���ݱ߳�ƽ���ıȹ�С�������ǰ��̭����ı��Σ������̭���࣬�����˱�������

			return 1.0;//���ݱ߳�ƽ���ıȹ�С�������ǰ��̭����ı���
		}
		return (dx1*dx2 + dy1*dy2) / Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);		
	}
	
	/**
	 * ����������ʶ���ͳ����״����
	 * @param srcImage
	 * @param CLImage
	 * @param outImage
	 * @param str
	 * @return
	 */
	public Mat Graphicdetection(Mat srcImage, Mat CLImage, Mat outImage,String str) {
		Mat bjImage = srcImage.clone();
		//��Ե���
		Imgproc.Canny(CLImage, outImage, 10, 100);	
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(100);
		Mat hierarchy = new Mat(outImage.rows(),outImage.cols(),CvType.CV_8UC1,new Scalar(0));
		 //��������
		Imgproc.findContours(outImage, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);		
		Moments[] mu = new Moments[contours.size()];
		Point[] mc = new Point[contours.size()];
		Point[] approx = new Point[100];
		
		int zfcount = 0;//����һ�������μ�����
		int sjcount = 0;//����һ�������μ�����		
		Point[] jx = new Point[10];
		Point[] sjx = new Point[10];
		
		for (int i = 0; i < contours.size(); i++) {
			//����������   
			mu[i] = Imgproc.moments(contours.get(i), false);
			//��������������   
			mc[i] = new Point(mu[i].get_m10()/mu[i].get_m00(),mu[i].get_m01()/mu[i].get_m00());
			//�������ĵ�
			Imgproc.circle(bjImage, mc[i], 5, new Scalar(0),-1,8,0);
			
			MatOfPoint2f mPoint2f1 = new MatOfPoint2f();
			MatOfPoint2f mPoint2f2 = new MatOfPoint2f();
			contours.get(i).convertTo(mPoint2f1, CvType.CV_32FC2);	
			//��������
			Imgproc.approxPolyDP(mPoint2f1,mPoint2f2, Imgproc.arcLength(mPoint2f1, true)*0.02, true);			
			mPoint2f2.convertTo(contours.get(i), CvType.CV_32S);			
			approx = mPoint2f2.toArray();
			
			if (approx.length == 4 && Math.abs(Imgproc.contourArea(new MatOfPoint(approx)))> 1000 && Imgproc.isContourConvex(new MatOfPoint(approx))){
				double maxCosine = 0;
				for (int j = 2; j < 5; j++) {
					double cosine = Math.abs(angle(approx[j % 4], approx[j -2], approx[j - 1]));//������
					maxCosine = Math.max(maxCosine, cosine);
			}
			if (maxCosine < 0.3) {
				jx = approx;
				//���Ƴ������α߿�
				Imgproc.line(bjImage, jx[0], jx[1], new Scalar(0, 0, 0), 3);
				Imgproc.line(bjImage, jx[1], jx[2], new Scalar(0, 0, 0), 3);
				Imgproc.line(bjImage, jx[2], jx[3], new Scalar(0, 0, 0), 3);
				Imgproc.line(bjImage, jx[3], jx[0], new Scalar(0, 0, 0), 3);

				String string = "Square " + str;
				Imgproc.putText(bjImage, string, new Point(mc[i].x, mc[i].y - 10), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0), 1);
				zfcount++;			
			 }
			}
			if (approx.length == 3) //������
			{
				sjx = approx;
				//���Ƴ������α߿�
				Imgproc.line(bjImage, sjx[0], sjx[1], new Scalar(0, 0, 0), 3);
				Imgproc.line(bjImage, sjx[1], sjx[2], new Scalar(0, 0, 0), 3);
				Imgproc.line(bjImage, sjx[2], sjx[0], new Scalar(0, 0, 0), 3);

				String string = "Triangle " + str;
				Imgproc.putText(bjImage, string, new Point(mc[i].x, mc[i].y - 10), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0), 1);
				sjcount++;
			}	
	  }
		//===============================================�����ǻ�����Բ======================================================//
		Mat circles = new Mat();		
		//��˹�˲�
		Imgproc.GaussianBlur(CLImage, CLImage, new Size(9,9), 0,0);
		//����Բ���
		Imgproc.HoughCircles(CLImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1.5, 10, 200, 100, 0, 0);
		for (int i = 0; i < circles.cols(); i++) 
		{			
			double vCircle[] = circles.get(0, i);			
			Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));		
			int radius = (int)Math.round(vCircle[2]);
			String string = "Round " + str;
			Imgproc.putText(bjImage, string, new Point(center.x, center.y - 10), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0), 1);			
			Imgproc.circle(bjImage, center, 3, new Scalar(0, 0, 0), -1, 8, 0);
			//����Բ����  
			Imgproc.circle(bjImage, center, radius, new Scalar(0, 0, 0), 3, 8, 0);
		}
		if (str == "R") {
			str = "��ɫ";
			System.out.printf("%s ������ �� %d ��\n",str, zfcount);
			System.out.printf("%s ������ �� %d ��\n",str, sjcount);
			System.out.printf("%s Բ     �� %d ��\n\n",str, circles.cols());
		} else if(str == "G") {
			str = "��ɫ";
			System.out.printf("%s ������ �� %d ��\n",str, zfcount);
			System.out.printf("%s ������ �� %d ��\n",str, sjcount);
			System.out.printf("%s Բ     �� %d ��\n\n",str, circles.cols());
		}else {
			str = "��ɫ";
			System.out.printf("%s ������ �� %d ��\n",str, zfcount);
			System.out.printf("%s ������ �� %d ��\n",str, sjcount);
			System.out.printf("%s Բ     �� %d ��\n\n",str, circles.cols());
		}		
		return bjImage;
    }
	/**
	 * ���������һЩ������Ϣ
	 */
	void ShowHelpText()
	{
		//�����ӭ��Ϣ��OpenCV�汾
		System.out.printf("\n\n\t\t\t��Ŀ˵��������һ��������Ŀ��\n");
		System.out.printf("\n\n\t\t\t��ӭʹ��OpenCV����Ⱥ�ţ�226503332����ӭѧϰ������\n");
		System.out.printf("\n\n\t\t\t��������Junys��д���ɹ���ʹ�ý�ֹ������\n");
		System.out.printf("\n\n  ----------------------------------------------------------------------------\n\n");
	}
}
