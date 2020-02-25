///*******************************************************************************
// * Copyright 2011-2013 Sergey Tarasevich
// * <p/>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p/>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p/>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *******************************************************************************/
//package com.xshell.xshelllib.tools.PictureBrowser;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.xshell.xshelllib.R;
//import com.xshell.xshelllib.logutil.LogUtils;
//
///**
// * ViewPager页面显示Activity
// *
// * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
// */
//public class ImagePagerActivity extends Activity {
//
//	private static final String STATE_POSITION = "STATE_POSITION";
//	public static final String IMAGES = "ARRAY_IMAGES";
//	public static final String START_URL = "com.nostra13.example.universalimageloader.IMAGES";
//	public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
//	DisplayImageOptions options;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	ViewPager pager;
//	private View lastChildView;
//    private boolean flag = true; //用于标记是否是第一次加载
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//去除title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		//去掉Activity上面的状态栏
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
//		setContentView(R.layout.ac_image_pager);
//
//		Bundle bundle = getIntent().getExtras();
//		String[] imageUrls = bundle.getStringArray(IMAGES);
//
//		String startUrl = bundle.getString(START_URL);
//
//		LogUtils.e("huang","图片："+imageUrls.toString());
//		LogUtils.e("huang","startUrl:"+startUrl);
//
//		// 当前显示View的位置
//		int pagerPosition = bundle.getInt(IMAGE_POSITION, 0);
//
//		// 如果之前有保存用户数据
//		if (savedInstanceState != null) {
//			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
//		}
//
//		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).resetViewBeforeLoading(true).cacheOnDisc(true)
//				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300)).build();
//
//		pager = (ViewPager) findViewById(R.id.pager);
//		pager.setAdapter(new ImagePagerAdapter(imageUrls));
//
//		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//			}
//
//			@Override
//			public void onPageSelected(int position) {
//				Log.i("zzy","----onPageSelected--:"+position);
//                View currentView;
//                currentView = pager.findViewById(position);
//                Log.i("zzy","-------currentView-----:"+currentView);
//				if(currentView ==  null) {  //代表第一次加载，因为第一次加载没有currentView,onPageSelected比填充数据还快
//                    flag = true;
//				} else {
//                    PhotoView photoView = (PhotoView) lastChildView.findViewById(R.id.image);
//					PhotoViewAttacher attacher = photoView.getAttacher();
//					//attacher.setScale(1);
//				}
//                lastChildView = currentView;
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//
//			}
//		});
//
//
//		if (null != startUrl) {  //不为空代表需要加载指定的页面
//			for (int i = 0; i < imageUrls.length; i++) {
//				if (imageUrls[i].equals(startUrl)) {
//					pagerPosition = i;
//					break;
//				}
//			}
//		}
//		//Log.i("zzy","pagerPosition:"+pagerPosition);
//		pager.setCurrentItem(pagerPosition); // 显示当前位置的View
//	}
//
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// 保存用户数据
//		outState.putInt(STATE_POSITION, pager.getCurrentItem());
//	}
//
//	private class ImagePagerAdapter extends PagerAdapter {
//
//		private String[] images;
//		private LayoutInflater inflater;
//
//		ImagePagerAdapter(String[] images) {
//			this.images = images;
//			inflater = getLayoutInflater();
//		}
//
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return super.getPageTitle(position);
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			((ViewPager) container).removeView((View) object);
//		}
//
//		@Override
//		public void setPrimaryItem(ViewGroup container, int position, Object object) {
//			super.setPrimaryItem(container, position, object);
//			//Log.i("zzy","--------setPrimaryItem----position----------:"+object);
//            if (flag) {  //代表第一次加载，需要在这里获取当前的view
//                lastChildView = (View) object;
//                flag = false;
//            }
//
//		}
//
//		@Override
//		public int getCount() {
//			return images.length;
//		}
//
//		@Override
//		public Object instantiateItem(ViewGroup view, int position) {
//            Log.i("zzy","-------instantiateItem-----:"+position);
//			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
//            imageLayout.setId(position);
//			final PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
//			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
//			imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					spinner.setVisibility(View.VISIBLE);
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//					String message = null;
//					switch (failReason.getType()) { // 获取图片失败类型
//					case IO_ERROR: // 文件I/O错误
//						message = "Input/Output error";
//						break;
//					case DECODING_ERROR: // 解码错误
//						message = "Image can't be decoded";
//						break;
//					case NETWORK_DENIED: // 网络延迟
//						message = "Downloads are denied";
//						break;
//					case OUT_OF_MEMORY: // 内存不足
//						message = "Out Of Memory error";
//						break;
//					case UNKNOWN: // 原因不明
//						message = "Unknown error";
//						break;
//					}
//					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();
//
//					spinner.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//					spinner.setVisibility(View.GONE); // 不显示圆形进度条
//				}
//			});
//
//			// 把得到的photoView放到这个负责变形的类当中
//			PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
//			mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置充满全屏
//
//			((ViewPager) view).addView(imageLayout, 0); // 将图片增加到ViewPager
//			return imageLayout;
//		}
//
//		@Override
//		public boolean isViewFromObject(View view, Object object) {
//			return view.equals(object);
//		}
//
//		@Override
//		public void restoreState(Parcelable state, ClassLoader loader) {
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//
//		@Override
//		public void startUpdate(View container) {
//		}
//	}
//}