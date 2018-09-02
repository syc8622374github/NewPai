package com.cyc.newpai.ui.me;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.util.ImageUtil;
import com.cyc.newpai.util.ScreenUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SuggestionActivity extends BaseActivity {

    private RadioButton rbSoftSystem;
    private RadioButton rbShopProgram;
    private EditText etContent;
    private GridView gvPicture;
    private List<String> images = new ArrayList<>();
    public static HashMap<String, Bitmap> imagesCache = new HashMap();//图像缓存类
    private GridViewAdapter gridViewAdapter;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final String TAG = SuggestionActivity.class.getSimpleName();
    private boolean isDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void initData() {
        images.add("-1");
        gvPicture.setAdapter(gridViewAdapter = new GridViewAdapter(images));
    }

    private void initView() {
        rbSoftSystem = findViewById(R.id.rb_suggestion_soft_system);
        rbShopProgram = findViewById(R.id.rb_suggestion_shop_program);
        etContent = findViewById(R.id.et_suggestion_content);
        gvPicture = findViewById(R.id.gv_suggestion_picture);
        ImageView iv = findViewById(R.id.iv_suggestion_show);
        gvPicture.setOnItemClickListener((parent, view, position, id) -> {
            if (!isDelete) {
                final String url = images.get(position);
                if (url.equals("-1")) {//调用图片选择
                    Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                } else {//查看上传图片
                    /*Intent intent = new Intent(ComplaintaAdvice.this,LookImageActivity.class);
                    intent.putStringArrayListExtra(LookImageActivity.IMG_PATH,images);
                    intent.putExtra(LookImageActivity.IMG_SELECT,position);
                    startActivity(intent);*/
                    //iv.setImageBitmap(imagesCache.get(images.get(position)));
                }
            } else {
                gridViewAdapter.notifyDataSetChanged();
            }
            isDelete = false;
        });
        gvPicture.setOnItemLongClickListener((parent, view, position, id) -> {
            String url = images.get(position);
            if (!url.equals("-1")) {
                isDelete = true;
                gridViewAdapter.notifyDataSetChanged();
            }
            return true;
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    /**
     * gridView 适配器
     */
    public class GridViewAdapter extends BaseAdapter {
        List<String> list;

        public GridViewAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(SuggestionActivity.this, R.layout.up_load_item, null);
            }
            convertView.setLayoutParams(new AbsListView.LayoutParams(ScreenUtil.getScreenWidth(SuggestionActivity.this) / 4, ScreenUtil.getScreenWidth(SuggestionActivity.this) / 4));
            final ImageView img = convertView.findViewById(R.id.image);
            ImageView delete = convertView.findViewById(R.id.delete);
            final String data = getItem(position);
            try {
                if (data.equals("-1")) {
                    img.setImageResource(R.drawable.complaint_camera_icon);
                    delete.setVisibility(View.GONE);
                } else {
                    GlideApp
                            .with(SuggestionActivity.this)
                            .load(new File(images.get(position)))
                            .override(ScreenUtil.px2dp(SuggestionActivity.this, 300), ScreenUtil.px2dp(SuggestionActivity.this, 300)) //设置大小
                            .into(img);
                    if (isDelete) {
                        delete.setVisibility(View.VISIBLE);
                    } else {
                        delete.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            delete.setOnClickListener(v -> {
                images.remove(data);
                imagesCache.remove(data);
                /*if(images.size()>1){
                    isDelete = true;
                }*/
                if(!images.get(0).equals("-1")){
                    images.add(0,"-1");
                }
                gridViewAdapter.notifyDataSetChanged();
            });
            convertView.setBackgroundColor(Color.parseColor("#00000000"));
            return convertView;
        }
    }

    /**
     * 选择图库图片后的回调
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    File file = new File(getFilePath(data.getData()));
                    if (!file.exists()) {
                        Log.i(TAG, "file not exists");
                        Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!images.contains(file.getPath())) {
                        images.add(file.getPath());
                        if(images.size()>4){
                            images.remove("-1");
                        }else if(!images.get(0).equals("-1")){
                            images.add(0,"-1");
                        }
                        Bitmap cache = null;
                        if(cache==null){
                            cache = ImageUtil.getImage(file.getPath());
                            //ImageUtil.saveBitmap(cache, Bitmap.CompressFormat.JPEG, Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiucall/cache/"+ WebService.stringToMD5(file.getPath())+".jpg",100);
                        }
                        imagesCache.put(file.getPath(),cache);
                        handler.post(() -> gridViewAdapter.notifyDataSetChanged());
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFilePath(Uri data) {
        String path = "";
        if (!TextUtils.isEmpty(data.getAuthority())) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data, proj, null, null, null);
            if (null == cursor) {
                Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
            } else if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        } else {
            path = data.getPath();
        }
        return path;
    }

    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_suggestion_ok:
                String type = rbSoftSystem.isChecked() ? "1" : "2";
                String contentStr = etContent.getText().toString();
                Map<String, String> params = new HashMap<>();
                if (TextUtils.isEmpty(contentStr)) {
                    ToastManager.showToast(this, "请输入反馈意见", Toast.LENGTH_SHORT);
                } else {
                    params.put("type", type);
                    params.put("content", contentStr);
                    Map<String,File> fileParams = new HashMap<>();
                    for(String image : images){
                        if(!image.equals("-1")){
                            fileParams.put(image,new File(image));
                        }
                    }
                    OkHttpManager.getInstance(this).postOfFileAsyncHttp(HttpUrl.HTTP_SUGGESTION_URL,params,imagesCache,new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG,e.getMessage()+"");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();
                                    ResponseBean<Object> data = getGson().fromJson(str, new TypeToken<ResponseBean<Object>>() {
                                    }.getType());
                                    if (data.getCode() == 200) {
                                        handler.post(()->ToastManager.showToast(SuggestionActivity.this, "感谢您反馈给我们的意见。", Toast.LENGTH_LONG));
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.post(()->ToastManager.showToast(SuggestionActivity.this, "反馈失败，请稍后再试。", Toast.LENGTH_LONG));
                        }
                    });
                }
                break;

        }
    }
}
