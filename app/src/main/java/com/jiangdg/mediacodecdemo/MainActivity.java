package com.jiangdg.mediacodecdemo;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jiangdg.mediacodecdemo.utils.CameraUtils;
import com.jiangdg.mediacodecdemo.utils.MediaMuxerUtils;
import com.jiangdg.mediacodecdemo.utils.SensorAccelerometer;

public class MainActivity extends Activity implements SurfaceHolder.Callback{
    private Button mBtnRecord;
    private SurfaceView mSurfaceView;
    private CameraUtils mCamManager;
    private boolean isRecording;
	//加速传感器
	private static SensorAccelerometer mSensorAccelerometer;

    private CameraUtils.OnPreviewFrameResult mPreviewListener = new CameraUtils.OnPreviewFrameResult() {
        @Override
        public void onPreviewResult(byte[] data, Camera camera) {
            mCamManager.getCameraIntance().addCallbackBuffer(data);
            MediaMuxerUtils.getMuxerRunnableInstance().addVideoFrameData(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamManager = CameraUtils.getCamManagerInstance(MainActivity.this);
		//实例化加速传感器
		mSensorAccelerometer = SensorAccelerometer.getSensorInstance();
		
        mSurfaceView = (SurfaceView)findViewById(R.id.main_record_surface);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCamManager.cameraFocus(new CameraUtils.OnCameraFocusResult() {
					@Override
					public void onFocusResult(boolean result) {
						if(result){
							Toast.makeText(MainActivity.this, "对焦成功", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
        
        mBtnRecord = (Button)findViewById(R.id.main_record_btn);
        mBtnRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaMuxerUtils mMuxerUtils = MediaMuxerUtils.getMuxerRunnableInstance();
                if(!isRecording){
                    mMuxerUtils.startMuxerThread();
                    mBtnRecord.setText("停止录像");
                }else{
                    mMuxerUtils.stopMuxerThread();
                    mBtnRecord.setText("开始录像");
                }
                isRecording = !isRecording;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamManager.setSurfaceHolder(surfaceHolder);
        mCamManager.setOnPreviewResult(mPreviewListener);
        mCamManager.createCamera();
        mCamManager.startPreview();
        startSensorAccelerometer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamManager.stopPreivew();
        mCamManager.destoryCamera();
        stopSensorAccelerometer();
    }
    
	private void startSensorAccelerometer() {
		// 启动加速传感器，注册结果事件监听器
		if (mSensorAccelerometer != null) {
			mSensorAccelerometer.startSensorAccelerometer(MainActivity.this,
					new SensorAccelerometer.OnSensorChangedResult() {
						@Override
						public void onStopped() {					
							// 对焦成功，隐藏对焦图标
							mCamManager.cameraFocus(new CameraUtils.OnCameraFocusResult() {
								@Override
								public void onFocusResult(boolean reslut) {
						
								}
							});
						}

						@Override
						public void onMoving(int x, int y, int z) {
//							Log.i(TAG, "手机移动中：x=" + x + "；y=" + y + "；z=" + z);
						}
					});
		}
	}

	private void stopSensorAccelerometer() {
		// 释放加速传感器资源
		if (mSensorAccelerometer == null) {
			return;
		}
		mSensorAccelerometer.stopSensorAccelerometer();
	}
}
