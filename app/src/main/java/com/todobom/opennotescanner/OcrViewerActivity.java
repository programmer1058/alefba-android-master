package com.todobom.opennotescanner;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.todobom.opennotescanner.db.Ocr;
import com.todobom.opennotescanner.helpers.Utils;
import com.todobom.opennotescanner.model.OcrResult;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrViewerActivity extends AppCompatActivity {
  AbsoluteLayout absoluteLayout;

  private ImageView mOcrImageView;
  private View mWaitSpinner;
  private ImageLoader mImageLoader;
  private TextView mTextResponse;
  private OcrRepository mOcrRepository;
  private AlertDialog.Builder deleteConfirmBuilder;
  private String mPath;
  private Ocr mOcr;
  private ActionBar actionBar;
  private String text;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  private void forceRTLIfSupported() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    forceRTLIfSupported();
    super.onCreate(savedInstanceState);
    mOcrRepository = new OcrRepository(getApplication());
    setContentView(R.layout.activity_ocr_viewer);
    init();


    // initialize Universal Image Loader
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
    mImageLoader = ImageLoader.getInstance();
    mImageLoader.init(config);

    Intent i = getIntent();
    mPath = i.getStringExtra("path");
    String isnew = i.getStringExtra("new");
    if (isnew.equals("true")) {
//TODO
    }

    // Load image, decode it to Bitmap and return Bitmap to callback
    mImageLoader.loadImage("file://" + mPath, new SimpleImageLoadingListener() {
      @Override
      public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mOcrImageView.setImageBitmap(loadedImage);
      }
    });

    mWaitSpinner.setVisibility(View.VISIBLE);
    actionBar.hide();
    mOcrRepository.getOcr(mPath).observe(this, new Observer<Ocr>() {
      @Override
      public void onChanged(@Nullable Ocr ocr) {
        if (ocr == null) {

          //Start request to Naghsh Server
          byte[] buffer = new byte[0];
          try {
            buffer = FileUtils.readFileToByteArray(new File(mPath));
          } catch (IOException e) {
            return;
          }
          String filename = mPath.split("/")[mPath.split("/").length - 1];


          MultipartBody.Part filePart = MultipartBody.Part.createFormData(
            "image",
            filename,
            RequestBody.create(MediaType.parse("image/png"), buffer)
          );

          retrofit2.Call<OcrResult> call = OpenNoteScannerApplication.getInstance().getRestClient().getApiService().readImage(filePart);
          call.enqueue(new Callback<OcrResult>() {
            @Override
            public void onResponse(Call<OcrResult> call, Response<OcrResult> response) {
              if (response.isSuccessful()) {
                OcrResult element = response.body();
                Ocr ocr = new Ocr(mPath, element);
                mOcrRepository.insert(ocr);
                actionBar.show();
                Log.i("ResponseResponse: ",response.body().toString());


              } else {
                Log.i("Nagsh Server Log", "not SuccessFul Response");
                Toast.makeText(getApplicationContext(), "خطای سرور!", Toast.LENGTH_LONG).show();
              }
            }

            @Override
            public void onFailure(Call<OcrResult> call, Throwable t) {
              mWaitSpinner.setVisibility(View.GONE);
              actionBar.show();
              Log.e("Error", "Exception", t);
              Toast.makeText(getApplicationContext(), "خطا در شبکه!", Toast.LENGTH_LONG).show();
            }
          });
          //End request to Naghsh server
        } else {

text= ocr.getOcrResult().getDocument().getText();
          Log.i("Response: ", ocr.getOcrResult().toString());

          //*************************************
          Log.i("Response: ", OpenNoteScannerActivity.pictureSizeWidth + "\t" + OpenNoteScannerActivity.pictureSizeHeight);
          int x = 0;
          int y = 0;
          int width = 0;
          int height = 0;
          int line_no = ocr.getOcrResult().getDocument().getParts().size();
          Log.i("LOGLOGLOG Response LN: ", line_no + "");
//          Toast.makeText(OcrViewerActivity.this,"AAAAAA",Toast.LENGTH_LONG).show();

          for (int i = 0; i < line_no; i++) {

            x = Integer.parseInt(ocr.getOcrResult().getDocument().getParts().get(i).getBox().split("\\s+")[0]);
            y = Integer.parseInt(ocr.getOcrResult().getDocument().getParts().get(i).getBox().split("\\s+")[1]);
            width = Integer.parseInt(ocr.getOcrResult().getDocument().getParts().get(i).getBox().split("\\s+")[2]);
            height = Integer.parseInt(ocr.getOcrResult().getDocument().getParts().get(i).getBox().split("\\s+")[3]);

//            Toast.makeText(OcrViewerActivity.this,"X:"+x+" Y:"+y+" H: "+height+" W: "+width ,Toast.LENGTH_LONG).show();
           /* Log.i("LOGLOGLOG Response",
              ocr.getOcrResult().getDocument().getParts().get(i).getType() +
                "\t" + ocr.getOcrResult().getDocument().getParts().get(i).getText() +
                "\t" +ocr.getOcrResult().getDocument().getParts().get(i).getBox() + "\n");*/

              float ratio=(float) OpenNoteScannerActivity.pictureSizeWidth/OpenNoteScannerActivity.pictureSizeHeight;
            if (ocr.getOcrResult().getDocument().getParts().get(i).getType().equals("text")) {
              TextView textView = new TextView(OcrViewerActivity.this);
//              textView.setTextSize(26);
              textView.setText("  " + ocr.getOcrResult().getDocument().getParts().get(i).getText());
              Log.i("LOGLOGLOG W " + i, "X:" + x + " Y:" + y + " H: " + height + " W: " + width + "\ttext: " + ocr.getOcrResult().getDocument().getParts().get(i).getText());

              Log.i("WWWWWWWWWWWW BB: ", width+" "+height+" "+x+" "+y+" R:" +ratio);

                  width/=ratio;
//                  width*=0.97;
                  height/=ratio;
//                  height*=1.2;
                  x/=ratio;
                  y/=ratio;

              AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width,height,x,y);

              Log.i("WWWWWWWWWWWW: ", width+" "+height+" "+x+" "+y);

              TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
              textView.setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
              //border
              GradientDrawable gd = new GradientDrawable();
              gd.setColor(0x7Fffffff); // Changes this drawbale to use a single color instead of a gradient
              gd.setCornerRadius(5);
              gd.setStroke(2, 0xFF000000);
              textView.setBackground(gd);
              textView.setLayoutParams(lp);
              absoluteLayout.addView(textView, lp);
            }

          }
          //*************************************
          mWaitSpinner.setVisibility(View.GONE);
          actionBar.show();
//                    getActionBar().show();
          mTextResponse.setText(ocr.getOcrResult().getDocument().getText()  );
          mTextResponse.setVisibility(View.VISIBLE);
          mOcr = ocr;
        }

      }
    });
  }


  private void init() {
    absoluteLayout = findViewById(R.id.absoluteLayout);
    mWaitSpinner = findViewById(R.id.wait_spinner);
    mOcrImageView = (ImageView) findViewById(R.id.ocr_image_view);
    mTextResponse = (TextView) findViewById(R.id.txtResponse);
    init_delete_dialog();
    actionBar = getSupportActionBar();
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setTitle(null);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_forward_black_24dp);
  }

  private void init_delete_dialog() {
    deleteConfirmBuilder = new AlertDialog.Builder(this);
    deleteConfirmBuilder.setTitle(getString(R.string.confirm_title));
    deleteConfirmBuilder.setMessage(getString(R.string.confirm_delete_text));
    deleteConfirmBuilder.setPositiveButton(getString(R.string.answer_yes), new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int which) {
        deleteImage();
        dialog.dismiss();
      }

    });
    deleteConfirmBuilder.setNegativeButton(getString(R.string.answer_no), new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_ocrviewer, menu);

    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.action_copy:
        copy();
        break;
      case android.R.id.home:
        finish();
        break;
      case R.id.action_share:
        shareImage();
        return true;
      case R.id.action_delete:
        deleteConfirmBuilder.create().show();
        return true;
//            case R.id.action_about:
//                FragmentManager fm = getSupportFragmentManager();
//                AboutFragment aboutDialog = new AboutFragment();
//                aboutDialog.show(fm, "about_view");
//                break;
      case R.id.action_visibility:
        if (item.isChecked()) {
          mTextResponse.setVisibility(View.VISIBLE);
          item.setIcon(getDrawable(R.drawable.ic_ios_image));
          item.setTitle(R.string.action_invisible);
          item.setChecked(false);
        } else {
          mTextResponse.setVisibility(View.INVISIBLE);
          item.setIcon(getDrawable(R.drawable.ic_ios_paper));
          item.setTitle(R.string.action_visible);
          item.setChecked(true);
        }

      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void copy() {
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("متن کپی شد.", text);
    clipboard.setPrimaryClip(clip);
  }

  private void deleteImage() {
    String filePath = mPath;
    final File photoFile = new File(filePath);

    photoFile.delete();
    Utils.removeImageFromGallery(filePath, this);
    mOcrRepository.delete(mOcr);

    finish();
  }

  public void shareImage() {
    final Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("image/jpg");
    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", new File(mPath));
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    Log.d("Fullscreen", "uri " + uri);

    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_snackbar)));
  }

}