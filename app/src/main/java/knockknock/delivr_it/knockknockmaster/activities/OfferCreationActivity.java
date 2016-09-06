package knockknock.delivr_it.knockknockmaster.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import knockknock.delivr_it.knockknockmaster.tasks.ImageAndDealUploader;
import knockknock.delivr_it.knockknockmaster.R;

public class OfferCreationActivity extends Activity {

    EditText title;
    EditText message;
    CheckBox allow_ordering;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_offer);

        title = (EditText) findViewById(R.id.editText_offer_title);
        message = (EditText) findViewById(R.id.editText_offer_message);
        allow_ordering = (CheckBox) findViewById(R.id.checkbox_allow_ordering);

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    displaySelectedImage(path.get(path.size() - 1));
                    imagePath = path.get(path.size() - 1);
                    break;
                }
        }
    }

    public void importAndShowImage(View view) {
        FishBun.with(OfferCreationActivity.this).startAlbum();
    }

    public void uploadData(View view) {
        String title = this.title.getText().toString();
        String message = this.message.getText().toString();
        boolean allow_ordering = this.allow_ordering.isChecked();

        if (title.isEmpty() || message.isEmpty() || imagePath.isEmpty()) {
            return;
        }

        try {
            new ImageAndDealUploader(OfferCreationActivity.this, title, message, imagePath).uploadImage();
        } catch (IOException e) {
        }
    }


    private void displaySelectedImage(String imagePath) {
        ImageView imageView = (ImageView) findViewById(R.id.image_selected_image);
        Picasso.with(getBaseContext()).load(new File(imagePath)).into(imageView);
    }


}
