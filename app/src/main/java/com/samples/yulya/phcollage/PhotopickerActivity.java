package com.samples.yulya.phcollage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PhotopickerActivity extends FragmentActivity {

    private String userLogin;
    private String act;
    private ArrayList<Image> list=null;
    private ArrayList <String> url=null;
    private GridView gr;
    private CheckBox mCheck1;
    private CheckBox mCheck2;
    private CheckBox mCheck3;
    private  EditText adress;
    ImageAdapter mAdapter;
    Fragment frag1;
    Fragment frag2;
    Fragment frag3;
    FragmentTransaction ftrans;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopicker);
        Intent myIntent = getIntent();
        userLogin = myIntent.getStringExtra("userLogin");
        act = myIntent.getStringExtra("ac");
        url=new ArrayList <String>();

        frag1 = new FragT();
        frag2 = new FragTh();
        frag3 = new FragF();

        imageLoader = ImageLoader.getInstance();
        File cacheDir = getApplicationContext().getCacheDir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(200, 200)
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader.init(config);

        gr=(GridView)findViewById(R.id.gridImage);
        new DownloadUrl().execute();

        gr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mAdapter.mThumb.get(position).getCheck() == "0") {
                    mAdapter.getView(position, view, adapterView ).setBackgroundResource(R.drawable.active);
                    mAdapter.mThumb.get(position).setCheck("1");
                }
                else
                {
                    mAdapter.getView(position, view, adapterView ).setBackgroundResource(R.drawable.passive);
                    mAdapter.mThumb.get(position).setCheck("0");}
            }
        });
        mCheck1 = (CheckBox)findViewById(R.id.ch1);
        mCheck2 = (CheckBox)findViewById(R.id.ch2);
        mCheck3 = (CheckBox)findViewById(R.id.ch3);
        final Button mOk = (Button) findViewById(R.id.ok);
        adress =  (EditText)findViewById(R.id.email);
        final Button butSend = (Button)findViewById(R.id.send);

        mCheck1.setOnClickListener(clickCh);
        mCheck2.setOnClickListener(clickCh);
        mCheck3.setOnClickListener(clickCh);
        mOk.setOnClickListener(clickBut);
        butSend.setOnClickListener(clickBut);

}
    View.OnClickListener clickCh = new View.OnClickListener() {

        public void onClick(View view) {
            ftrans = getSupportFragmentManager().beginTransaction();
            switch (view.getId()) {
                case R.id.ch1:
                    ftrans.replace(R.id.container, frag1);
                    break;
                case R.id.ch2:
                    ftrans.replace(R.id.container, frag2);
                    break;
                case R.id.ch3:
                    ftrans.replace(R.id.container, frag3);
                    break;
                default:
                    break;
            }
            ftrans.commit();
        }
    };
    View.OnClickListener clickBut = new View.OnClickListener() {

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ok:
                    fillListUrl(url);
                    if (mCheck1.isChecked()) {
                        if (url.size()>1) {
                            setImage((ImageView) frag1.getView().findViewById(R.id.im1), (ImageView) frag1.getView().findViewById(R.id.im2));
                        } else {Toast.makeText(getApplication(), "Укажите выбранное количество фотографий.", Toast.LENGTH_SHORT).show();
                            url.clear();
                            break;}
                    } else if (mCheck2.isChecked()) {
                        if (url.size()>2) {
                            setImage((ImageView) frag2.getView().findViewById(R.id.im1), (ImageView) frag2.getView().findViewById(R.id.im2), (ImageView) frag2.getView().findViewById(R.id.im3));
                        } else {Toast.makeText(getApplication(), "Укажите выбранное количество фотографий.", Toast.LENGTH_SHORT).show();
                            url.clear();break;}
                    } else if (mCheck3.isChecked()) {
                        if (url.size()>3) {
                            setImage((ImageView) frag3.getView().findViewById(R.id.im1), (ImageView) frag3.getView().findViewById(R.id.im2), (ImageView) frag3.getView().findViewById(R.id.im3), (ImageView) frag3.getView().findViewById(R.id.im4));
                        } else {Toast.makeText(getApplication(), "Укажите выбранное количество фотографий.", Toast.LENGTH_SHORT).show();
                            url.clear();break;}
                    } else {
                        Toast.makeText(getApplication(), "Укажите количество фото для коллажа.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    ((View) findViewById(R.id.layout2)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.layout1)).setVisibility(View.GONE);
                    break;
                case R.id.send:
                    View layout = (View) findViewById(R.id.container);
                    makeCollage(layout, Environment.getExternalStorageDirectory().getAbsolutePath() + "/Collage/");

                    if (adress.getText().length()==0) { Toast.makeText(getApplication(), "Укажите свой E-mail.", Toast.LENGTH_SHORT).show();}
                    else {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{adress.getText().toString()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Фотоколлаж");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Фотоколлаж");
                        emailIntent.putExtra(
                                android.content.Intent.EXTRA_STREAM,
                                Uri.parse("file://"
                                        + Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/Collage/collage.png")
                        );

                        try {

                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            PhotopickerActivity.this.finish();

                        } catch (android.content.ActivityNotFoundException ex) {

                            Toast.makeText(getApplication(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
  public class DownloadUrl extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String APIURL = "https://api.instagram.com/v1";
                String UseRiD = "https://api.instagram.com/v1/users/search";
                list = new ArrayList<Image>();
                JSONParser jParser = new JSONParser();

                String url = UseRiD + "?q=" + userLogin + "&access_token=" + act;
                JSONObject resp = jParser.getJSONFromUrl(url);
                JSONArray  jsonArray = resp.getJSONArray("data");
                String user_id = jsonArray.getJSONObject(0).getString("id");

                String urlString = APIURL + "/users/"+ user_id + "/media/recent/?access_token=" + act;
                JSONObject resp1 = jParser.getJSONFromUrl(urlString);

                JSONArray  jsonArray1 = resp1.getJSONArray("data");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                         JSONObject mainImageJsonObject = jsonArray1.getJSONObject(i).getJSONObject("images").getJSONObject("thumbnail");
                                list.add(new Image(mainImageJsonObject.getString("url"),"0"));
                    }

            }
            catch (JSONException e)
            {
               e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(list.size()==0){
                Toast.makeText(getApplication(), "Чтобы сделать коллаж, загрузите фото в Instagram.", Toast.LENGTH_SHORT).show();
                PhotopickerActivity.this.finish();
            }
            mAdapter = new ImageAdapter(PhotopickerActivity.this,list);
            gr.setAdapter(mAdapter);
        }
    }
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photopicker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void fillListUrl(ArrayList<String> list){
        for (int i=0;i< mAdapter.getCount();++i){
            if (mAdapter.mThumb.get(i).getCheck()=="1") {
                list.add(mAdapter.mThumb.get(i).getUrl());

            }
        }
    }

    public void makeCollage(View view, String path) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        try {
            saveLayout(bitmap, path);
        } catch (Exception e) {
        }
    }

    public void saveLayout(Bitmap b, String path) {
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Collage/");
        if (!directory.exists()) {
            directory.mkdirs();
        } else {

            File file = new File(path, "collage.png");
            if (file.exists()) file.delete();
            FileOutputStream fOut;
            try {

                fOut = new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.PNG, 90, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }
    }
    public void  setImage(ImageView imv1, ImageView imv2) {

        imv1.setImageDrawable(new BitmapDrawable(getResources(), imageLoader.loadImageSync(url.get(0))));
        imv2.setImageDrawable(new BitmapDrawable(getResources(), imageLoader.loadImageSync(url.get(1))));

    }
    public  void  setImage(ImageView imv1, ImageView imv2, ImageView imv3) {

        setImage(imv1, imv2);
        imv3.setImageDrawable(new BitmapDrawable(getResources(), imageLoader.loadImageSync(url.get(2))));


    }
    public  void  setImage(ImageView imv1, ImageView imv2, ImageView imv3, ImageView imv4){

        setImage(imv1, imv2, imv3);
        imv4.setImageDrawable(new BitmapDrawable(getResources(), imageLoader.loadImageSync(url.get(3))));

    }
}
